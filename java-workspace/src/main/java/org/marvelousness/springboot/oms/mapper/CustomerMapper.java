package org.marvelousness.springboot.oms.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.marvelousness.springboot.oms.entity.pojo.Customer;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-08-31 22:55
 */
public interface CustomerMapper {
	public static final String TABLE_NAME = "`oms_customer`";
	/**
	 * 判断指定的主键是否存在
	 * @param id
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE `id` = #{id}")
	public Boolean exists(Long id);
	/**
	 * 删除指定的数据（逻辑删除）
	 * @param id
	 * @return
	 */
	@Update("UPDATE " + TABLE_NAME + " SET `is_deleted` = 1 WHERE `id` = #{id}")
	public Integer delete(Long id);
	/**
	 * 根据主键查询客户信息
	 * @param id
	 * @return
	 */
	public Customer selectByPrimaryKey(Long id);
	/**
	 * 根据主键跟新一条数据
	 * @param customer
	 * @return
	 */
	public Integer update(Customer customer);
	/**
	 * 插入一条数据
	 * @param customer
	 * @return
	 */
	public Integer insert(Customer customer);
}