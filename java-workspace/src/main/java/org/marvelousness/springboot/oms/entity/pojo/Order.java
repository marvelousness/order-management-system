package org.marvelousness.springboot.oms.entity.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 订单信息 - 用于映射 oms_order 表
 * 
 * @author 981247127@qq.com
 * @time 2020-08-16 18:09
 */
@Data
public class Order {
	private Long id;
	/**
	 * 订单编号
	 */
	private String number;
	/**
	 * 该订单应归属于哪个客户，参考 customer 表的主键
	 */
	private Long customerId;
	/**
	 * 订单创建人的ID，参考 user 表的主键
	 */
	private Long creatorId;
	/**
	 * 订单执行人的ID，参考 user 表的主键
	 */
	private Long executorId;
	/**
	 * 订单服务金额
	 */
	private BigDecimal amount;
	/**
	 * 该订单的成交时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dealTime;
	/**
	 * 该订单的创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 订单备注
	 */
	private String remark;
	/**
	 * 订单付款记录
	 */
	private List<OrderPayment> payments;
}