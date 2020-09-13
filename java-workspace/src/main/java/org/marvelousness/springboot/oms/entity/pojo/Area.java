package org.marvelousness.springboot.oms.entity.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 地区
 * @author 981247127@qq.com
 * @time 2020-09-05 08:54
 */
@Data
public class Area {
	private Integer id;
	/**
	 * 地区的名称
	 */
	private String name;
	/**
	 * 下级地区
	 */
	@JsonProperty("children")
	private List<Area> areas;
}