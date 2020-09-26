package org.marvelousness.springboot.oms.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.marvelousness.springboot.basic.exception.ServiceInvokeException;
import org.marvelousness.springboot.basic.utils.StringUtils;
import org.marvelousness.springboot.oms.entity.ResponsePageEntity;
import org.marvelousness.springboot.oms.entity.dto.OrderDto;
import org.marvelousness.springboot.oms.entity.pojo.Order;
import org.marvelousness.springboot.oms.entity.pojo.OrderPayment;
import org.marvelousness.springboot.oms.mapper.OrderDtoMapper;
import org.marvelousness.springboot.oms.mapper.OrderMapper;
import org.marvelousness.springboot.oms.mapper.OrderPaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单服务
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 16:41
 */
@Service
public class OrderService {
	@Autowired
	private OrderMapper mapper;
	@Autowired
	private OrderDtoMapper dtoMapper;
	@Autowired
	private OrderPaymentMapper paymentMapper;

	/**
	 * 保存新订单
	 * 
	 * @param order
	 * @return
	 */
	@Transactional
	public String saveNewOrder(Order order) {
		// 校验入参
		if (order == null) {
			return null;
		}
		List<OrderPayment> payments = order.getPayments();
		if (payments == null || payments.isEmpty()) {
			throw new ServiceInvokeException("该订单没有付款信息，不允许被创建！");
		}
		payments.forEach((OrderPayment payment) -> {
			BigDecimal amount = payment.getAmount();
			if (amount == null || BigDecimal.ZERO.compareTo(amount) > 0) {
				throw new ServiceInvokeException("该订单的实际付款金额不允许输入负数");
			}
			if (StringUtils.isBlank(payment.getPaymentAccount())) {
				throw new ServiceInvokeException("该订单的付款账号不允许为空");
			}
			if (StringUtils.isBlank(payment.getPaymentProof())) {
				throw new ServiceInvokeException("该订单的付款凭证不允许为空");
			}
		});
		// 生产订单编号
		String number = mapper.nextOrderNumber();
		order.setNumber(number);
		// 保存订单信息
		mapper.insert(order);
		Long id = order.getId();
		if (id == null || id < 1) {
			throw new ServiceInvokeException("新建订单失败");
		}
		// 保存付款信息
		payments.forEach((OrderPayment payment) -> {
			payment.setOrderId(order.getId());
			payment.setAgentId(order.getCreatorId());
		});
		paymentMapper.inserts(payments);
		return number;
	}

	/**
	 * 获取所有订单的支付方式
	 * 
	 * @return
	 */
	public List<String> getOrderPayWays() {
		List<String> ways = paymentMapper.selectOrderPayWays();
		if (ways == null || ways.isEmpty()) {
			// 在数据库中没有数据的时候，返回常用的支付方式的集合
			ways = Arrays.asList("信用卡", "储蓄卡", "支付宝", "QQ支付", "微信支付", "苹果支付", "现金支付");
		}
		return ways;
	}

	/**
	 * 分页查询客户列表数据
	 * 
	 * @param creator  精准过滤条件 - 创建人的ID
	 * @param executor 精准过滤条件 - 执行人的ID
	 * @param keyword  模糊过滤条件 - 客户信息关键词
	 * @param number   页码数量
	 * @param size     页项数
	 * @return
	 */
	public ResponsePageEntity<OrderDto> list(Long creator, Long executor, String keyword, Integer number, Integer size) {
		int limit = size != null && size > 0 ? size : 10;
		int offset = number != null && number > 0 ? (number - 1) * limit : 0;
		List<OrderDto> dtos = dtoMapper.select(creator, executor, keyword, offset, limit);
		Integer total = dtoMapper.count(creator, executor, keyword);
		return new ResponsePageEntity<OrderDto>(dtos, total, number, size);
	}

	/**
	 * 分配订单执行人
	 * 
	 * @param orderNumbers
	 * @param executorId
	 * @return
	 */
	public Integer distributeOrderExecutor(List<String> orderNumbers, Long executorId) {
		if (orderNumbers == null || orderNumbers.isEmpty() || executorId == null || executorId < 1) {
			return 0;
		}
		// 移除集合中所有为空和为 null 的元素
		orderNumbers.removeAll(Collections.singleton(null));
		orderNumbers.removeAll(Collections.singleton(""));
		if (orderNumbers.isEmpty()) {
			return 0;
		}
		return mapper.updateOrderExecutor(orderNumbers, executorId);
	}

}
