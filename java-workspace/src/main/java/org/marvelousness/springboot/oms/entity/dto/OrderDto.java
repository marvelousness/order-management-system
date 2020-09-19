package org.marvelousness.springboot.oms.entity.dto;

import java.math.BigDecimal;
import java.util.List;

import org.marvelousness.springboot.oms.entity.pojo.Customer;
import org.marvelousness.springboot.oms.entity.pojo.Order;
import org.marvelousness.springboot.oms.entity.pojo.OrderPayment;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 *
 * @author 981247127@qq.com
 * @time 2020-08-31 23:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderDto extends Order {
	/**
	 * 创建人姓名
	 */
	private String creatorName;
	/**
	 * 该创建人所属的部门的ID
	 */
	private Long creatorDepartmentId;
	/**
	 * 该创建人所属的部门的名称
	 */
	private String creatorDepartmentName;

	/**
	 * 执行人姓名
	 */
	private String executorName;
	/**
	 * 该执行人所属的部门的ID
	 */
	private Long executorDepartmentId;
	/**
	 * 该执行人所属的部门的名称
	 */
	private String executorDepartmentName;

	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 客户昵称
	 */
	private String customerNick;
	/**
	 * 客户的旺旺号
	 */
	private String customerWangwang;
	/**
	 * 客户的QQ号
	 */
	private String customerQQ;
	/**
	 * 客户的微信号
	 */
	private String customerWechat;
	/**
	 * 客户的手机号
	 */
	private String customerPhone;

	/**
	 * 已付总额
	 * @return
	 */
	public BigDecimal getPaidAmount() {
		// 统计所有的付款信息的总额
		BigDecimal amount = BigDecimal.ZERO;
		List<OrderPayment> payments = super.getPayments();
		if (payments != null) {
			for (OrderPayment payment : payments) {
				if (payment != null && payment.getAmount() != null) {
					amount = amount.add(payment.getAmount());
				}
			}
		}
		return amount;
	}
	
	/**
	 * 表示当前订单是否已经付清
	 * @return
	 */
	public boolean isPayOff() {
		// 0 <= PaidAmount - amount;
		return BigDecimal.ZERO.compareTo(getPaidAmount().subtract(getAmount())) < 1;
	}
	
	/**
	 * 设置创建人信息
	 * 
	 * @param user
	 */
	public void setCreator(UserDto user) {
		if (user == null) {
			return;
		}
		this.creatorName = user.getName();
		this.creatorDepartmentId = user.getDepartmentId();
		this.creatorDepartmentName = user.getDepartmentName();
	}

	/**
	 * 设置所属人信息
	 * 
	 * @param user
	 */
	public void setExecutor(UserDto user) {
		if (user == null) {
			return;
		}
		this.executorName = user.getName();
		this.executorDepartmentId = user.getDepartmentId();
		this.executorDepartmentName = user.getDepartmentName();
	}

	/**
	 * 设置客户信息
	 * 
	 * @param user
	 */
	public void setCustomer(Customer customer) {
		if (customer == null) {
			return;
		}

		this.customerName = customer.getName();
		this.customerNick = customer.getNick();
		this.customerWangwang = customer.getWangwang();
		this.customerQQ = customer.getQq();
		this.customerWechat = customer.getWechat();
		this.customerPhone = customer.getPhone();

	}
}
