package org.marvelousness.springboot.oms.entity.dto;

import org.marvelousness.springboot.oms.entity.pojo.Customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 *
 * @author 981247127@qq.com
 * @time 2020-08-31 23:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerDto extends Customer {
	/**
	 * 创建人姓名
	 */
	private String creatorName;
	/**
	 * 该员工所属的部门的ID
	 */
	private Long creatorDepartmentId;
	/**
	 * 该员工所属的部门的名称
	 */
	private String creatorDepartmentName;

	/**
	 * 设置创建人信息
	 * 
	 * @param user
	 */
	public void setCreator(UserDto user) {
		if (user == null) {
			return;
		}
		this.creatorName = user.getName();
		this.creatorDepartmentId = user.getDepartmentId();
		this.creatorDepartmentName = user.getDepartmentName();
	}
}
