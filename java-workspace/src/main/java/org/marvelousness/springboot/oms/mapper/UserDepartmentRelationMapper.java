package org.marvelousness.springboot.oms.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 981247127@qq.com
 */
public interface UserDepartmentRelationMapper {
	public static final String TABLE_NAME = "`oms_sys_department_user_relation`";

	/**
	 * 
	 * @param departmentId 部门的ID
	 * @param userId       用户的ID
	 * @param isLeader     该用户是否是该部门的领导
	 * @return
	 */
	@Select("INSERT INTO " + TABLE_NAME + " (`dep_id`, `user_id`, `is_leader`) VALUES (#{did}, #{uid}, #{is});")
	public void insert(@Param("did") Long departmentId, @Param("uid") Long userId, @Param("is") boolean isLeader);

	/**
	 * 查询某个部门的领导的用户ID
	 * 
	 * @param departmentId 部门的ID
	 * @return
	 */
	@Select("SELECT `user_id` FROM " + TABLE_NAME + " WHERE `dep_id` = #{did} AND `is_leader` = 1 LIMIT 1")
	public Long getLeader(@Param("did") Long departmentId);
	
	/**
	 * 删除用户和部门之间的关系
	 * 
	 * @param departmentId 部门的ID
	 * @param userId       用户的ID
	 * @return
	 */
	@Delete("DELETE FROM " + TABLE_NAME + " WHERE `user_id` = #{uid}")
	public int delete(@Param("uid") Long userId);

}