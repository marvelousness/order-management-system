package org.marvelousness.springboot.oms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.marvelousness.springboot.oms.entity.pojo.Department;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-08-16 18:49
 */
public interface DepartmentMapper {
	public static final String TABLE_NAME = "`oms_sys_department`";

	/**
	 * 根据父级ID查询所有子级的部门树的信息
	 * 
	 * @param pid
	 * @return
	 */
	public List<Department> selectDepartmentTreeByParentId(Long pid);

	/**
	 * 查询指定的部门ID在数据库中是否存在
	 * 
	 * @param id
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE id = #{id}")
	public Boolean exists(Long id);
}