package org.marvelousness.springboot.oms.controller;

import static org.marvelousness.springboot.basic.enumer.AuthorizationType.Anon;

import org.marvelousness.springboot.basic.annotation.Authorization;
import org.marvelousness.springboot.basic.session.SessionManager;
import org.marvelousness.springboot.basic.session.SessionUser;
import org.marvelousness.springboot.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 认证控制器
 *
 * @author 981247127@qq.com
 * @time 2020-08-13 18:05
 */
@Controller
@RequestMapping(path = { "authentication", "session" })
@Authorization(Anon)
public class AuthenticationController {
	@Autowired
	private UserService service;
	@Autowired
	private SessionManager manager;

	/**
	 * 认证登录或者绑定账号，该函数是将前端的 LoginAction.js
	 * 
	 * @mail 981247127@qq.com
	 * @time 2020-08-13 18:05
	 * @param authenticator 登录身份，可以是登录账号，手机号，邮箱
	 * @param certification 登录凭据，也就是登录密码
	 * @param certifycode   登录验证码
	 * @param user          当前会话中的用户信息，仅当已登录需要绑定账号的时候有效
	 * @return
	 */
	@ResponseBody
	@PostMapping("login")
	public ResponseEntity<?> login(String authenticator, String certification, String certifycode, SessionUser user) {
		if (user != null && user.getId() != null) {
			return new ResponseEntity<String>("OK!", HttpStatus.OK);
		}
		service.login(authenticator, certification, certifycode);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	/**
	 * 会话注销
	 * 
	 * @return
	 */
	@GetMapping("logout")
	public String logout() {
		manager.logout();
		return "redirect:/login";
	}
}