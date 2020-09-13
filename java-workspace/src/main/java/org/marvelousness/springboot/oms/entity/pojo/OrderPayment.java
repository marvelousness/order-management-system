package org.marvelousness.springboot.oms.entity.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 订单付款信息 - 用于映射 oms_order_payment 表
 * 
 * @author 981247127@qq.com
 * @time 2020-08-16 18:12
 */
@Data
public class OrderPayment {
	private Long id;
	/**
	 * 订单的ID，参考 order 表的主键
	 */
	private Long orderId;
	/**
	 * 付款信息受理人的ID，参考 user 表的主键
	 */
	private Long agentId;
	/**
	 * 付款金额
	 */
	private BigDecimal amount;
	/**
	 * 数据创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 付款时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date paymentTime;
	/**
	 * 付款账号
	 */
	private String paymentAccount;
	/**
	 * 付款凭证
	 */
	private String paymentProof;
	/**
	 * 付款方式
	 */
	private String paymentWay;
}