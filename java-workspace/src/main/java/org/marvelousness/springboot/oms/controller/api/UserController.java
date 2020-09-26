package org.marvelousness.springboot.oms.controller.api;

import java.util.List;

import org.marvelousness.springboot.basic.annotation.SessionId;
import org.marvelousness.springboot.oms.entity.ResponsePageEntity;
import org.marvelousness.springboot.oms.entity.dto.AutoCompleteDto;
import org.marvelousness.springboot.oms.entity.dto.UserDto;
import org.marvelousness.springboot.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * 
 * @author 981247127@qq.com
 * @time 2020-08-17 16:07
 */
@RestController
@RequestMapping("api/user")
public class UserController {
	@Autowired
	private UserService service;

	/**
	 * 为前端的 AutoComplete 提供数据支持
	 * 
	 * @return
	 */
	@GetMapping("auto-complete")
	public List<AutoCompleteDto> list() {
		return service.getAutoCompleteOptions();
	}

	/**
	 * 查询用户列表
	 * 
	 * @param number 页码数
	 * @param size   页项数
	 * @return
	 */
	@GetMapping("list")
	public ResponsePageEntity<UserDto> list(Integer number, Integer size) {
		return service.list(number, size);
	}

	/**
	 * 保存用户信息，用于新增和修改用户信息
	 * 
	 * @param user 用户信息
	 * @return
	 */
	@PostMapping("save")
	public Long save(@RequestBody UserDto user) {
		return service.save(user);
	}

	/**
	 * 修改账号的密码
	 * 
	 * @param passowrd 新密码
	 * @return
	 */
	@PostMapping("passwd")
	public int passwd(@SessionId Long id, String passowrd) {
		return service.passwd(id, passowrd);
	}

	/**
	 * 修改账号的状态
	 * 
	 * @param id 被启用或停用的账号的ID
	 * @return
	 */
	@PostMapping("status/{disabled}")
	public Integer status(@PathVariable("disabled") String disabled, Long id) {
		if ("disable".equals(disabled)) {
			return service.changeUserStatus(id, Boolean.TRUE);
		} else if ("enable".equals(disabled)) {
			return service.changeUserStatus(id, Boolean.FALSE);
		}
		return 0;
	}

	/**
	 * 删除用户
	 * 
	 * @param id 被删除的用户的ID
	 * @return
	 */
	@PostMapping("delete")
	public Integer delete(Long id) {
		return service.delete(id);
	}
}