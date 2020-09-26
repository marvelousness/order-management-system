package org.marvelousness.springboot.oms.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.marvelousness.springboot.oms.entity.dto.DashboardStatistics;
import org.marvelousness.springboot.oms.mapper.DashboardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-09-26 22:36
 */
@Service
public class DashboardService {
	@Autowired
	private DashboardMapper mapper;
	final String KEY_YEAR = "year";
	final String KEY_SUM = "sum";
	final String KEY_COUNT = "count";

	/**
	 * 提供首页仪表盘统计数据
	 * 
	 * @return
	 */
	public DashboardStatistics getDashboardStatistics() {
		DashboardStatistics statistics = new DashboardStatistics();
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		int lastYear = thisYear - 1;
		// 统计每年的订单数量
		List<Map<String, Object>> orders = mapper.selectStatisticsOrder();
		// 统计每年的客户数量
		List<Map<String, Object>> customers = mapper.selectStatisticsCustomer();
		// 统计每年的收入金额
		List<Map<String, Object>> incomes = mapper.selectStatisticsOrderAmount();
		if (orders != null) {
			// 订单
			for (Map<String, Object> order : orders) {
				Integer year = (Integer) order.get(KEY_YEAR);
				BigDecimal sum = order.get(KEY_COUNT) != null ? new BigDecimal(order.get(KEY_COUNT).toString()) : BigDecimal.ZERO;
				if (year != null) {
					if (year.equals(thisYear)) {
						statistics.setThisYearNewOrder(sum);
					} else if (year.equals(lastYear)) {
						statistics.setLastYearNewOrder(sum);
					}
				}
				if (statistics.getLastYearNewOrder() != null && statistics.getThisYearNewOrder() != null) {
					break;
				}
			}
		}
		if (customers != null) {
			// 客户
			for (Map<String, Object> customer : customers) {
				Integer year = (Integer) customer.get(KEY_YEAR);
				BigDecimal count = customer.get(KEY_COUNT) != null ? new BigDecimal(customer.get(KEY_COUNT).toString()) : BigDecimal.ZERO;
				if (year != null) {
					if (year.equals(thisYear)) {
						statistics.setThisYearNewCustomer(count);
					} else if (year.equals(lastYear)) {
						statistics.setLastYearNewCustomer(count);
					}
				}
				if (statistics.getLastYearNewCustomer() != null && statistics.getThisYearNewCustomer() != null) {
					break;
				}
			}
		}
		if (incomes != null) {
			// 收入
			for (Map<String, Object> income : incomes) {
				Integer year = (Integer) income.get(KEY_YEAR);
				BigDecimal count = income.get(KEY_SUM) != null ? new BigDecimal(income.get(KEY_SUM).toString()) : BigDecimal.ZERO;
				if (year != null) {
					if (year.equals(thisYear)) {
						statistics.setThisYearIncome(count);
					} else if (year.equals(lastYear)) {
						statistics.setLastYearIncome(count);
					}
				}
				if (statistics.getLastYearIncome() != null && statistics.getThisYearIncome() != null) {
					break;
				}
			}
		}
		return statistics;
	}
}
