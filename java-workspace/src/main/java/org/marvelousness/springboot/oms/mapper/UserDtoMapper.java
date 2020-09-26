package org.marvelousness.springboot.oms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.marvelousness.springboot.oms.entity.dto.UserDto;

/**
 * @author 981247127@qq.com
 */
public interface UserDtoMapper {
	/**
	 * 根据主键查询单条数据
	 * 
	 * @param offset 便宜量
	 * @param limit  限制量
	 * @return
	 */
	public UserDto selectByPrimaryKey(Long id);
	/**
	 * 查询列表
	 * 
	 * @param offset 便宜量
	 * @param limit  限制量
	 * @return
	 */
	public List<UserDto> select(@Param("offset") int offset, @Param("limit") int limit);

	/**
	 * 查询总数
	 * 
	 * @return
	 */
	public Integer count();
}