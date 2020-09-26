package org.marvelousness.springboot.oms.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

/**
 * 仪表盘
 * 
 * @author 981247127@qq.com
 * @time 2020-09-26 22:28
 */
public interface DashboardMapper {
	/**
	 * 统计每年的收入情况
	 * 
	 * @return
	 */
	@Select("SELECT SUM(`amount`) AS 'sum', YEAR(`create_time`) AS 'year' FROM `oms_order_payment` GROUP BY YEAR(`create_time`);")
	public List<Map<String, Object>> selectStatisticsOrderAmount();

	/**
	 * 统计每年的新订单数量
	 * 
	 * @return
	 */
	@Select("SELECT COUNT(id) AS 'count', YEAR(`create_time`) AS 'year' FROM `oms_order` GROUP BY YEAR(`create_time`);")
	public List<Map<String, Object>> selectStatisticsOrder();

	/**
	 * 统计每年的新客户数量
	 * 
	 * @return
	 */
	@Select("SELECT COUNT(id) AS 'count', YEAR(`create_time`) AS 'year' FROM `oms_customer` GROUP BY YEAR(`create_time`);")
	public List<Map<String, Object>> selectStatisticsCustomer();
}
