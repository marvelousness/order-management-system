package org.marvelousness.springboot.oms.entity.pojo;

import java.util.List;

import lombok.Data;

/**
 * 部门信息 - 用于映射 oms_sys_department 表
 * 
 * @author 981247127@qq.com
 * @time 2020-08-16 18:39
 */
@Data
public class Department {
	private Long id;
	/**
	 * 父部门id，根部门为0
	 */
	private Long pid;
	/**
	 * 部门名称
	 */
	private String name;
	/**
	 * 表示该数据已经被禁用
	 */
	private Boolean disabled;
	/**
	 * 该部门下的所有的用户
	 */
	private List<User> users;
	/**
	 * 该部门下的直系部门
	 */
	private List<Department> departments;
}