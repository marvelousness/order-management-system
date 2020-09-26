package org.marvelousness.springboot.oms.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.marvelousness.springboot.oms.entity.pojo.Customer;
import org.marvelousness.springboot.oms.entity.pojo.Order;
import org.marvelousness.springboot.oms.entity.pojo.OrderPayment;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-09-26 09:45
 */
public interface MySQLMapper {
	/**
	 * 查询当前的数据库的名称
	 * @return
	 */
	@Select("SELECT DATABASE();")
	public String database();
	/**
	 * 获取订单表中下一个订单的订单编号，它有三部分组成：
	 * <ol>
	 * <li><strong>·前导位·</strong> 固定字母 MO, 表示 marvelousness`s order</li>
	 * <li><strong>·时间位·</strong> 长度 14，包含年月日时分秒，例如 20200905165551</li>
	 * <li><strong>·自增位·</strong> 长度 10， 有数据库自增字段补零组成，例如 0000000001</li>
	 * </ol>
	 * <p>
	 * 它的长度应该是 26，例如：MO202009051702260000000001
	 * </p>
	 * 
	 * @return
	 */
	@Select("SELECT CONCAT('MO', #{time}, LPAD(t.AUTO_INCREMENT, 10, '0')) FROM `information_schema`.`TABLES` t WHERE t.`TABLE_SCHEMA` = DATABASE() AND t.TABLE_NAME = 'oms_order'")
	public String mockNextOrderNumber(@Param("time") String time);
	/**
	 * 批量插入客户信息
	 * @param customers
	 * @return
	 */
	public Integer insertCustomer(Customer customer);
	/**
	 * 批量插入客订单信息
	 * @param customers
	 * @return
	 */
	public Integer insertOrder(Order order);
	/**
	 * 批量插入客订单付款凭证信息
	 * 
	 * @param customer
	 * @return
	 */
	public Integer insertOrderPayments(@Param("payments") List<OrderPayment> payments);
}