package org.marvelousness.springboot.oms.controller.api;

import java.util.List;

import org.marvelousness.springboot.oms.entity.pojo.Area;
import org.marvelousness.springboot.oms.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 地区控制器
 * @author 981247127@qq.com
 * @time 2020-09-05 10:58
 */
@RestController
@RequestMapping("api/area")
public class AreaController {
	@Autowired
	private AreaService service;
	
	@GetMapping("tree")
	public List<Area> tree() {
		return service.getAreas();
	}
}