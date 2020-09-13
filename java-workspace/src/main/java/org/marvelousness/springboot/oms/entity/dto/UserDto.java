package org.marvelousness.springboot.oms.entity.dto;

import org.marvelousness.springboot.oms.entity.pojo.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-08-22 15:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDto extends User {
	private static final long serialVersionUID = -364293398338833183L;
	/**
	 * 该员工所属的部门的ID
	 */
	private Long departmentId;
	/**
	 * 该员工所属的部门的名称
	 */
	private String departmentName;
	/**
	 * 该用户是否是该部门的领导
	 */
	private Boolean isLeader;
}
