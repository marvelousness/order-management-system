package org.marvelousness.springboot.oms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.marvelousness.springboot.oms.entity.pojo.OrderPayment;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-09-05 16:32
 */
public interface OrderPaymentMapper {
	public static final String TABLE_NAME = "`oms_order_payment`";

	/**
	 * 判断指定的主键是否存在
	 * 
	 * @param id
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE `id` = #{id}")
	public Boolean exists(Long id);

	/**
	 * 根据订单ID获取付款信息
	 * 
	 * @param id
	 * @return
	 */
	public List<OrderPayment> selectByOrderId(Long id);

	/**
	 * 插入一条数据
	 * 
	 * @param customer
	 * @return
	 */
	public Integer inserts(@Param("payments") List<OrderPayment> payments);

	/**
	 * 查询所有订单的支付方式
	 * 
	 * @return
	 */
	@Select("SELECT `payment_way` FROM " + TABLE_NAME + " GROUP BY `payment_way`")
	public List<String> selectOrderPayWays();
}