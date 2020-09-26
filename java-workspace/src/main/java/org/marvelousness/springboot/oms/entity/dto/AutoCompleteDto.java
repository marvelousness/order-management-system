package org.marvelousness.springboot.oms.entity.dto;

import java.util.List;

import lombok.Data;

/**
 * 为前端的 AutoComplete 提供数据支持
 * @author 981247127@qq.com
 * @time 2020-08-22 15:19
 */
@Data
public class AutoCompleteDto {
	/**
	 * 主键
	 */
	private String value;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 子节点
	 */
	private List<AutoCompleteDto> children;
}
