package org.marvelousness.springboot.oms.service;

import java.util.ArrayList;
import java.util.List;

import org.marvelousness.springboot.oms.entity.pojo.Department;
import org.marvelousness.springboot.oms.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 部门信息业务层
 * 
 * @author 981247127@qq.com
 * @time 2020-08-16 18:58
 */
@Service
public class DepartmentService {
	@Autowired
	private DepartmentMapper mapper;

	/**
	 * 获取部门树
	 * 
	 * @return
	 */
	public List<Department> getDepartmentTree() {
		List<Department> departments = mapper.selectDepartmentTreeByParentId(0L);
		if (departments == null) {
			departments = new ArrayList<Department>();
		}
		return departments;
	}

	/**
	 * 查询指定的部门ID在数据库中是否存在
	 * 
	 * @param id
	 * @return
	 */
	public boolean exists(Long id) {
		return Boolean.TRUE.equals(mapper.exists(id));
	}
}