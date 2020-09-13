package org.marvelousness.springboot.oms.controller.api;

import java.util.List;

import org.marvelousness.springboot.oms.entity.pojo.Department;
import org.marvelousness.springboot.oms.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门控制器
 * 
 * @author 981247127@qq.com
 * @time 2020-08-16 19:00
 */
@RestController
@RequestMapping("api/department")
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 获取部门树
	 * 
	 * @return
	 */
	@GetMapping("tree")
	public List<Department> tree() {
		return departmentService.getDepartmentTree();
	}
}