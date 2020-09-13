package org.marvelousness.springboot.oms.configurer;

import java.util.List;

import org.marvelousness.springboot.basic.resolver.SessionUserMethodArgumentResolver;
import org.marvelousness.springboot.oms.resolver.FrontEndViewResolver;
import org.marvelousness.springboot.oms.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc 配置
 * 
 * @author 981247127@qq.com
 * @time 2020-08-15 16:01
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 8 * 60 * 60)
public class WebMvcBasicConfigurer implements WebMvcConfigurer {

	@Autowired
	private SessionUserMethodArgumentResolver resolver;

	@Autowired
	private FileSystemService fService;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 将所有的静态资源定位到 statics 目录下
		registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/favicon.ico");
		registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/static/");
		String fs = fService.getFileSystemStoreAbsolutePath("");
		registry.addResourceHandler("/fs/resources/**").addResourceLocations("file:" + fs);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(resolver);
	}

	@Bean
	public FrontEndViewResolver globalErrorViewResolver() {
		return new FrontEndViewResolver();
	}
}