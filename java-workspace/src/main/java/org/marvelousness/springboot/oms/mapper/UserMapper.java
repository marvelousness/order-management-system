package org.marvelousness.springboot.oms.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.marvelousness.springboot.oms.entity.pojo.User;

/**
 * @author 981247127@qq.com
 */
public interface UserMapper {
	public static final String TABLE_NAME = "`oms_sys_user`";

	/**
	 * 根据 userid 来查询信息是否存在
	 * 
	 * @param userid
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE `userid` = #{userid} AND `is_deleted` = 0")
	public Boolean existsByUserid(String userid);

	/**
	 * 根据 userid 来查询信息是否存在
	 * 
	 * @param userid
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE `userid` = #{userid}")
	public Boolean existsByUseridWithoutDeleted(String userid);

	/**
	 * 根据 手机号码 来查询信息是否存在
	 * 
	 * @param mobile 手机号码
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE `mobile` = #{mobile} AND `is_deleted` = 0")
	public Boolean existsByMobile(String mobile);

	/**
	 * 根据 手机号码 来查询信息是否存在
	 * 
	 * @param mobile 手机号码
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE `mobile` = #{mobile}")
	public Boolean existsByMobileWithoutDeleted(String mobile);

	/**
	 * 根据 电子邮箱 来查询信息是否存在
	 * 
	 * @param mobile 电子邮箱
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE `email` = #{email} AND `is_deleted` = 0")
	public Boolean existsByEmail(String email);

	/**
	 * 根据 电子邮箱 来查询信息是否存在
	 * 
	 * @param mobile 电子邮箱
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE `email` = #{email}")
	public Boolean existsByEmailWithoutDeleted(String email);

	/**
	 * 查询本月的用户的数据的总数
	 * 
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE DATE_FORMAT(`create_time`, '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m')")
	public Integer countOfThisMonth();

	/**
	 * 根据主键查询用户的数据
	 * 
	 * @param id
	 * @return
	 */
	public User selectByPrimaryKey(Long id);

	/**
	 * 根据 userid 来查询信息
	 * 
	 * @param userid
	 * @param password 登录密码（非必要参数）
	 * @return
	 */
	public User selectByUserid(@Param("userid") String userid, @Param("password") String password);

	/**
	 * 根据 手机号码 来查询信息
	 * 
	 * @param mobile   手机号码
	 * @param password 登录密码（非必要参数）
	 * @return
	 */
	public User selectByMobile(@Param("mobile") String mobile, @Param("password") String password);

	/**
	 * 根据 电子邮箱 来查询信息
	 * 
	 * @param mobile   电子邮箱
	 * @param password 登录密码（非必要参数）
	 * @return
	 */
	public User selectByEmail(@Param("email") String email, @Param("password") String password);

	/**
	 * 新增用户信息
	 * 
	 * @param user
	 * @return
	 */
	public Integer insert(User user);

	/**
	 * 根据主键更新用户信息
	 * 
	 * @param user
	 * @return
	 */
	public Integer update(User user);

	/**
	 * 根据主键更新用户密码
	 * 
	 * @param user
	 * @return
	 */
	@Update("UPDATE " + TABLE_NAME + " SET `password`=MD5(#{passowrd}) WHERE (`id`=#{id});")
	public Integer updatePassword(@Param("id") Long id, @Param("passowrd") String passowrd);

	/**
	 * 禁用账号
	 * 
	 * @param user
	 * @return
	 */
	@Update("UPDATE " + TABLE_NAME + " SET `is_disabled`=1 WHERE (`id`=#{id});")
	public Integer disabled(Long id);

	/**
	 * 启用账号
	 * 
	 * @param user
	 * @return
	 */
	@Update("UPDATE " + TABLE_NAME + " SET `is_disabled`=0 WHERE (`id`=#{id});")
	public Integer enable(Long id);

	/**
	 * 删除账号
	 * 
	 * @param user
	 * @return
	 */
	@Update("UPDATE " + TABLE_NAME + " SET `is_deleted`=1 WHERE (`id`=#{id});")
	public Integer delete(Long id);
}