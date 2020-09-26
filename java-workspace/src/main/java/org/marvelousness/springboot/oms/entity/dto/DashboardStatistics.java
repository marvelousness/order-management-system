package org.marvelousness.springboot.oms.entity.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 首页仪表盘统计
 * 
 * @author 981247127@qq.com
 * @time 2020-09-26 22:37
 */
@Data
public class DashboardStatistics {
	/**
	 * 今年总收入
	 */
	private BigDecimal thisYearIncome;
	/**
	 * 去年总收入
	 */
	private BigDecimal lastYearIncome;
	/**
	 * 今年订单数量
	 */
	private BigDecimal thisYearNewOrder;
	/**
	 * 去年订单数量
	 */
	private BigDecimal lastYearNewOrder;
	/**
	 * 今年客户数量
	 */
	private BigDecimal thisYearNewCustomer;
	/**
	 * 去年客户数量
	 */
	private BigDecimal lastYearNewCustomer;

	/**
	 * 新订单增长率
	 * 
	 * @return
	 */
	public BigDecimal getGrowthRatioNewOrder() {
		if (thisYearNewOrder == null || thisYearNewOrder.compareTo(BigDecimal.ZERO) < 1 || lastYearNewOrder == null) {
			return BigDecimal.ZERO;
		}
		if (lastYearNewOrder.compareTo(BigDecimal.ZERO) < 1) {
			// 上一年没有新增任何数据
			return new BigDecimal(100);
		}
		try {
			return thisYearNewOrder.subtract(lastYearNewOrder).divide(lastYearNewOrder, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		} catch (Exception e) {
			e.printStackTrace();
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 新客户增长率
	 * 
	 * @return
	 */
	public BigDecimal getGrowthRatioNewCustomer() {
		if (thisYearNewCustomer == null || thisYearNewCustomer.compareTo(BigDecimal.ZERO) < 1 || lastYearNewCustomer == null) {
			return BigDecimal.ZERO;
		}
		if (lastYearNewCustomer.compareTo(BigDecimal.ZERO) < 1) {
			// 上一年没有新增任何数据
			return new BigDecimal(100);
		}
		try {
			return thisYearNewCustomer.subtract(lastYearNewCustomer).divide(lastYearNewCustomer, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		} catch (Exception e) {
			e.printStackTrace();
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 总收入增长率
	 * 
	 * @return
	 */
	public BigDecimal getGrowthRatioIncome() {
		if (thisYearIncome == null || thisYearIncome.compareTo(BigDecimal.ZERO) < 1 || lastYearIncome == null) {
			return BigDecimal.ZERO;
		}
		if (lastYearIncome.compareTo(BigDecimal.ZERO) < 1) {
			// 上一年没有新增任何数据
			return new BigDecimal(100);
		}
		try {
			return thisYearIncome.subtract(lastYearIncome).divide(lastYearIncome, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		} catch (Exception e) {
			e.printStackTrace();
			return BigDecimal.ZERO;
		}
	}
}