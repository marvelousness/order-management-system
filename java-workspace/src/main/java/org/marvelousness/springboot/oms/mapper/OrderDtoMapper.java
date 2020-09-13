package org.marvelousness.springboot.oms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.marvelousness.springboot.oms.entity.dto.OrderDto;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-08-31 22:55
 */
public interface OrderDtoMapper {
	/**
	 * 根据主键查询单条数据
	 * 
	 * @param id 客户ID
	 * @return
	 */
	public OrderDto selectByPrimaryKey(@Param("id") Long id);

	/**
	 * 查询列表
	 * 
	 * @param offset 便宜量
	 * @param limit  限制量
	 * @return
	 */
	public List<OrderDto> select(@Param("offset") Integer offset, @Param("limit") Integer limit);

	/**
	 * 查询总数
	 * 
	 * @return
	 */
	public Long count();
}