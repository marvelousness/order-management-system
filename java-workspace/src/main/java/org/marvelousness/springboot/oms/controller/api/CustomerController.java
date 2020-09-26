package org.marvelousness.springboot.oms.controller.api;

import org.marvelousness.springboot.basic.annotation.SessionId;
import org.marvelousness.springboot.oms.entity.ResponsePageEntity;
import org.marvelousness.springboot.oms.entity.dto.CustomerDto;
import org.marvelousness.springboot.oms.entity.pojo.Customer;
import org.marvelousness.springboot.oms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户控制器
 *
 * @author 981247127@qq.com
 * @time 2020-08-31 23:35
 */
@RestController
@RequestMapping("api/customer")
public class CustomerController {

	@Autowired
	private CustomerService service;

	/**
	 * 查询客户列表
	 * 
	 * @param keyword 模糊过滤条件 - 客户信息关键词
	 * @param number  页码数
	 * @param size    页项数
	 * @return
	 */
	@GetMapping("list")
	public ResponsePageEntity<CustomerDto> list(String keyword, Integer number, Integer size, @SessionId Long id) {
		// 检索订单创建人是我的客户信息
		return service.list(id, keyword, number, size);
	}

	/**
	 * 根据主键获取客户信息
	 * 
	 * @param id 客户ID
	 * @return
	 */
	@GetMapping("get")
	public CustomerDto get(Long id) {
		return service.getCustomer(id);
	}

	/**
	 * 保存用户信息，用于新增和修改用户信息
	 * 
	 * @param user 用户信息
	 * @return
	 */
	@PostMapping("save")
	public Long save(@RequestBody Customer customer, @SessionId Long sid) {
		if (customer != null) {
			customer.setCreatorId(sid);
		}
		return service.save(customer);
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