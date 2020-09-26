package org.marvelousness.springboot.oms.controller.api;

import org.marvelousness.springboot.oms.entity.dto.DashboardStatistics;
import org.marvelousness.springboot.oms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计控制器
 * 
 * @author 981247127@qq.com
 * @time 2020-09-26 23:10
 */
@RestController
@RequestMapping("api/statistics")
public class StatisticsController {
	@Autowired
	private DashboardService service;

	@GetMapping
	public DashboardStatistics getStatistics() {
		return service.getDashboardStatistics();
	}
}