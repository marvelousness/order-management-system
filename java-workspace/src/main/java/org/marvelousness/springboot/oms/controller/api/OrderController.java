package org.marvelousness.springboot.oms.controller.api;

import java.util.List;

import org.marvelousness.springboot.basic.annotation.SessionId;
import org.marvelousness.springboot.oms.entity.ResponsePageEntity;
import org.marvelousness.springboot.oms.entity.dto.OrderDto;
import org.marvelousness.springboot.oms.entity.pojo.Order;
import org.marvelousness.springboot.oms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-09-12 08:49
 */
@RestController
@RequestMapping("api/order")
public class OrderController {

	@Autowired
	private OrderService service;

	/**
	 * 查询客户列表
	 * 
	 * @param number 页码数
	 * @param size   页项数
	 * @return
	 */
	@GetMapping("list")
	public ResponsePageEntity<OrderDto> list(Integer number, Integer size) {
		return service.list(number, size);
	}

	/**
	 * 保存订单信息
	 * 
	 * @param order 订单对象
	 * @return
	 */
	@PostMapping("save")
	public String save(@RequestBody Order order, @SessionId Long id) {
		if (order != null) {
			order.setCreatorId(id);
		}
		return service.saveNewOrder(order);
	}

	/**
	 * 获取所有的订单支付方式
	 * 
	 * @return
	 */
	@GetMapping("payment/ways")
	public List<String> orderPayWays() {
		return service.getOrderPayWays();
	}
}