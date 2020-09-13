package org.marvelousness.springboot.oms.entity.pojo;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.marvelousness.springboot.basic.session.SessionUser;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 用户信息 - 用于映射 oms_sys_user 表
 * 
 * @author 981247127@qq.com
 */
@Data
public class User implements SessionUser {
	private static final long serialVersionUID = 3886084218698327762L;
	private Long id;
	/**
	 * 用户ID，代表员工的工号，也是用作登录的用户名
	 */
	private String userid;
	/**
	 * 手机号码，也是用作登录的手机号码
	 */
	private String mobile;
	/**
	 * 电子邮箱，也是用作登录的邮箱
	 */
	private String email;
	/**
	 * 登录凭据，用作登录时的密码
	 */
	private String password;
	/**
	 * 员工名字
	 */
	private String name;
	/**
	 * 员工性别，0表示女，其他非零的值均被视作性别男
	 */
	private Boolean gender;
	/**
	 * 入职日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date hiredDate;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 表示该数据已经被禁用
	 */
	private Boolean disabled;
	/**
	 * 员工头像，当值为 default 的时候，采用默认的头像
	 */
	private String avatar;
	
	/**
	 * 工龄（年）
	 * 
	 * @return
	 */
	public int getWorkingYears() {
		return (int) getHiredWorking(Calendar.YEAR);
	}

	/**
	 * 工龄（月）
	 * 
	 * @return
	 */
	public int getWorkingMonths() {
		return (int) getHiredWorking(Calendar.MONTH);
	}

	/**
	 * 获取工作的时间
	 * 
	 * @param field 来自 Calendar 的字段成员，例如 Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR, Calendar.DAY_OF_YEAR
	 * @return
	 */
	private long getHiredWorking(int field) {
		if (hiredDate == null) {
			return 0L;
		}
		// 一年是 31536000000 毫秒, 计算入职日期到现在的毫秒差值来换算成年
		long milliseconds = (System.currentTimeMillis() - hiredDate.getTime()) / 31536000000L;
		if (Calendar.SECOND == field) {
			// 计算相差的秒
			return milliseconds / 1000;
		} else if (Calendar.MINUTE == field) {
			// 计算相差的分钟
			return milliseconds / 1000 / 60;
		} else if (Calendar.HOUR == field) {
			// 计算相差的小时数
			return milliseconds / 1000 / 60 / 60;
		} else if (Calendar.DAY_OF_YEAR == field) {
			// 计算相差的天数
			return milliseconds / 1000 / 60 / 60 / 24;
		} else if (Calendar.MONTH == field) {
			// 计算相差的月数
			return milliseconds / 1000 / 60 / 60 / 24 / 30;
		} else if (Calendar.YEAR == field) {
			// 计算相差的月数
			return milliseconds / 1000 / 60 / 60 / 24 / 30 / 365;
		}
		return milliseconds;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.userid;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return !Boolean.TRUE.equals(disabled);
	}
}