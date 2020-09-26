package org.marvelousness.springboot.oms.controller.api;

import java.util.Arrays;
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
 * 订单控制器
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
	 * 查询订单列表
	 * 
	 * @param keyword 模糊过滤条件 - 客户信息关键词
	 * @param number  页码数
	 * @param size    页项数
	 * @return
	 */
	@GetMapping("list")
	public ResponsePageEntity<OrderDto> list(String keyword, Integer number, Integer size, @SessionId Long id) {
		// 检索订单创建人或者所属人是我的订单信息
		return service.list(id, id, keyword, number, size);
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
	 * 分配订单
	 * 
	 * @param numbers  订单编号
	 * @param executor 执行人ID
	 * @return
	 */
	@PostMapping("distribute")
	public Integer distribute(String[] numbers, Long executor) {
		if (numbers == null || executor == null) {
			return 0;
		}
		return service.distributeOrderExecutor(Arrays.asList(numbers), executor);
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