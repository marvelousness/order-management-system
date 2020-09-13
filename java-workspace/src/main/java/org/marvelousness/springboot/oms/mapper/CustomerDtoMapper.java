package org.marvelousness.springboot.oms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.marvelousness.springboot.oms.entity.dto.CustomerDto;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-08-31 22:55
 */
public interface CustomerDtoMapper {
	/**
	 * 根据主键查询单条数据
	 * 
	 * @param id 客户ID
	 * @return
	 */
	public CustomerDto selectByPrimaryKey(@Param("id") Long id);

	/**
	 * 查询列表
	 * 
	 * @param offset 便宜量
	 * @param limit  限制量
	 * @return
	 */
	public List<CustomerDto> select(@Param("offset") Integer offset, @Param("limit") Integer limit);

	/**
	 * 查询总数
	 * 
	 * @return
	 */
	public Long count();
}