package org.marvelousness.springboot.oms.entity.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 客户信息 - 用于映射 oms_customer 表
 * 
 * @author 981247127@qq.com
 * @time 2020-08-16 18:05
 */
@Data
public class Customer {
	private Long id;
	/**
	 * 该条数据创建的时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**
	 * 该条数据的创建者的ID，参考 user 表的主键
	 */
	private Long creatorId;
	/**
	 * 客户名称
	 */
	private String name;
	/**
	 * 客户昵称
	 */
	private String nick;
	/**
	 * 客户的旺旺号
	 */
	private String wangwang;
	/**
	 * 客户的QQ号
	 */
	private String qq;
	/**
	 * 客户的微信号
	 */
	private String wechat;
	/**
	 * 客户的手机号
	 */
	private String phone;
	/**
	 * 地域
	 */
	private String region;
	/**
	 * 备注内容
	 */
	private String remark;
}