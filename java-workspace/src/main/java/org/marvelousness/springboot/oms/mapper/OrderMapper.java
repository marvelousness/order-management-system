package org.marvelousness.springboot.oms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.marvelousness.springboot.oms.entity.pojo.Order;

/**
 * 
 *
 * @author 981247127@qq.com
 * @time 2020-09-05 16:32
 */
public interface OrderMapper {
	public static final String TABLE_NAME = "`oms_order`";

	/**
	 * 判断指定的订单是否存在
	 * 
	 * @param id
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE `number` = #{id}")
	public Boolean exists(String id);

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
	@Select("SELECT CONCAT('MO', DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), LPAD(t.AUTO_INCREMENT, 10, '0')) FROM `information_schema`.`TABLES` t WHERE t.`TABLE_SCHEMA` = DATABASE() AND t.TABLE_NAME = 'oms_order'")
	public String nextOrderNumber();

	/**
	 * 插入一条数据
	 * 
	 * @param customer
	 * @return
	 */
	public Long insert(Order order);
	
	/**
	 * 更新订单执行人
	 * @param numbers 订单编号
	 * @param executor 订单执行人
	 * @return
	 */
	public Integer updateOrderExecutor(@Param("numbers") List<String> numbers, @Param("executor") Long executor);
}