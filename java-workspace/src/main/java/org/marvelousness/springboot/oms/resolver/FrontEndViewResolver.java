package org.marvelousness.springboot.oms.resolver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.ParseException;
import org.marvelousness.springboot.basic.session.SessionManager;
import org.marvelousness.springboot.basic.session.SessionUser;
import org.marvelousness.springboot.basic.utils.NetworkUtil;
import org.marvelousness.springboot.basic.utils.StringUtils;
import org.marvelousness.springboot.oms.properties.StaticResourceProperties;
import org.marvelousness.springboot.oms.properties.nodejs.NodeMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * 前端视图解析器，因为前端的 Vue-Router 采用的是 History 模式，故而，当页面刷新的时候会出现404的情况，这里统一将所有的异常视图定位给前端，交给VUE来处理
 * 
 * @author xiaolinzi
 * @mail 981247127@qq.com
 * @time 2019-10-17 18:25
 */
@Slf4j
public class FrontEndViewResolver implements ErrorViewResolver {
	/**
	 * 设定登录的页面
	 */
	private static final String LOGIN_PATTERN = "/login";
	/**
	 * 表示node服务器的地址，用于开发环境下联调
	 */
	@Autowired
	private StaticResourceProperties properties;
	/**
	 * 会话管理对象
	 */
	@Autowired
	private SessionManager manager;
	/**
	 * 标识当前环境是否是调试环境
	 */
	@Value("${debug:false}")
	private Boolean isDebugger;

	/**
	 * 显示前端页面，一般来讲，参数 model中的数据是如下格式：
	 * 
	 * <pre>
	 * {
	 * 		"timestamp": "Thu Oct 17 18:57:53 CST 2019",
	 * 		"status": 404,
	 * 		"error": "Not Found",
	 * 		"message": "No message available",
	 *		"path": "/login"
	 * }
	 * </pre>
	 */
	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
		long start = System.nanoTime();
		ModelAndView view = new ModelAndView("vue-index");
		// 当前登录的用户
		final SessionUser user = manager.getSession();
		// 当前请求的地址
		final String path = getCurrentPath(model);
		// 标识用户是否已经登录
		final boolean logged = user != null && user.getId() != null;
		// 当前浏览器是否被支持响应一个HTML5的页面
		boolean supported = false;
		// 获取当前时间，虽然暂时没什么用
		// final Date timestamp = getDateTime(model);

		// 打印请求头
		{
			Enumeration<String> names = request.getHeaderNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				String header = request.getHeader(name);
				if (StringUtils.isBlank(name)) {
					continue;
				}
				log.info("Resolve View Header --> " + name + ":\t" + header);
				header = header == null ? "" : header.trim().toLowerCase();
			}

			// 判断当前浏览器是否支持 HTML5
			{
				// 根据 user-agent 请求头可以得到浏览器的内核信息，这里认为 Chrome 和 Firefox 支持 HTML5
				// Chrome 提供的值是：
				// Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36
				// 360 浏览器提供的是：
				// Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36
				// Firefox 提供的是：
				// Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0
				// IE 提供的是：
				// Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko
				String userAgent = request.getHeader("user-agent");
				userAgent = userAgent != null ? userAgent.toLowerCase() : "";
				supported = userAgent.contains("webkit") || userAgent.contains("firefox");
			}
		}

		// 路由访问拦截
		{

			String[] anons = properties.getAnons();
			// 用户没有登录的情况下
			boolean matched = false;
			AntPathMatcher matcher = new AntPathMatcher();
			if (anons != null && anons.length > 0) {
				for (String pattern : anons) {
					if (matcher.match(pattern, path)) {
						matched = true;
						break;
					}
				}
			}

			String reference = path;
			try {
				reference = URLEncoder.encode(path, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// e.printStackTrace();
			}
			String reason = "forbidden";
			long timestamp = System.currentTimeMillis();

			if (logged) {

				if (matcher.match(LOGIN_PATTERN, path)) {
					// 在已经登录的情况下，又打开了登录页面，则跳转到首页
					reason = "redirect-form-logged";
					return new ModelAndView("redirect:/?reason=" + reason + "&path=" + reference + "&t=" + timestamp);
				}

			} else if (!matched) {
				// 如果用户在没有登录的情况下直接访问前端的某些地址，如果不是匿名允许被访问的情况下，进行拦截

				try {
					reference = URLEncoder.encode(path, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				reason = "unauthorized-access";
				return new ModelAndView("redirect:" + LOGIN_PATTERN + "?reason=" + reason + "&path=" + reference + "&t=" + timestamp);

			}
		}

		//
		// 以此为分界线，路由拦截逻辑写在上方 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
		// ===================================================================================================================================
		// ===================================================================================================================================
		// 以此为分界线，页面传值逻辑写在下方 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		//

		// 注入需要加载的 js 资源或者 css 资源
		List<String> jses = new ArrayList<>();
		List<String> csses = new ArrayList<>();
		// 确定是否是本地开发模式
		String server = properties.getServer();
		boolean nodejsServerIsAlive = true;
		{
			// 判断是本地开发模式的条件是：
			// 1. 配置文件中开启了debug
			// 2. 当前主机为 window 操作系统
			// 3. 请求 nodejs 服务器有响应

			if (StringUtils.isNoneBlank(server)) {
				try {
					NetworkUtil.sslGet(server);
				} catch (URISyntaxException | ParseException e) {
					// URL 地址错误，直接忽略
					e.printStackTrace();
				} catch (IOException e) {
					// 如果连接出现异常，则说明目标主机已经宕机
					nodejsServerIsAlive = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// 根据当前环境判断需要加载本地资源还是远程资源
		List<NodeMapping> mappings = Boolean.TRUE.equals(isDebugger) ? properties.getDebugMappings() : properties.getReleaseMappings();
		if (mappings != null) {
			AntPathMatcher matcher = new AntPathMatcher();
			for (NodeMapping mapping : mappings) {
				// 这里根据不同的路由加载不同的资源
				if (!matcher.match(mapping.getHref(), path)) {
					continue;
				}
				List<String> _jses = mapping.getJses();
				List<String> _csses = mapping.getCsses();
				if (_jses != null) {
					jses.addAll(mapping.getJses());
				}
				if (_csses != null) {
					csses.addAll(mapping.getCsses());
				}
			}
		}

		String title = "奇思妙想工作室 - 订单管理系统";
		if (StringUtils.isNoneBlank(properties.getTitle(), properties.getVersion())) {
			title = properties.getTitle() + "  " + properties.getVersion();
		}

		if (!Boolean.TRUE.equals(isDebugger) && !nodejsServerIsAlive) {
			// 当前环境处于非本地开发模式，且前端资源站如果没有响应，则加载站内资源
			server = "";
		}

		// 向页面注入变量
		view.addObject("title", title);
		view.addObject("jses", jses);
		view.addObject("csses", csses);
		view.addObject("server", server);
		view.addObject("supported", supported);
		view.addObject("debug", isDebugger);
		view.addObject("isLogin", logged);
		view.addObject("uid", logged ? user.getId() : 0);
		// 静态资源的过期时间设置为 1分钟，即每隔一分钟更新一次静态文件
		view.addObject("expire", System.currentTimeMillis() / 6000L);

		view.setStatus(HttpStatus.OK);

		long timeDiff = System.nanoTime() - start;
		log.info("处理路由[" + path + "]用时：" + timeDiff + " ns.约：" + BigDecimal.valueOf(timeDiff).divide(BigDecimal.valueOf(1000000), RoundingMode.HALF_UP) + " ms.");
		return view;
	}

	/**
	 * 获取当前请求的时间
	 * 
	 * @author xiaolinzi
	 * @mail 981247127@qq.com
	 * @time 2019-10-17 19:06
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	private Date getDateTime(Map<String, Object> model) {
		Date timestamp = null;
		Object object = model.get("timestamp");
		if (object != null) {
			try {
				timestamp = (Date) object;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		if (timestamp == null) {
			timestamp = new Date();
		}
		return timestamp;
	}

	/**
	 * 获取当前请求的路径
	 * 
	 * @author xiaolinzi
	 * @mail 981247127@qq.com
	 * @time 2019-10-17 19:06
	 * @param model
	 * @return
	 */
	private String getCurrentPath(Map<String, Object> model) {
		String path = "/";
		Object object = model.get("path");
		if (object != null) {
			path = object.toString();
		}
		return path;
	}
}