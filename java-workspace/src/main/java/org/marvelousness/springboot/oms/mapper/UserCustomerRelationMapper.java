package org.marvelousness.springboot.oms.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author 981247127@qq.com
 */
public interface UserCustomerRelationMapper {

	/**
	 * 查询指定的用户是否存在订单
	 * 
	 * @param userid
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM `oms_customer` WHERE `creator_id` = #{userid}")
	public Boolean exists(Long userid);

}