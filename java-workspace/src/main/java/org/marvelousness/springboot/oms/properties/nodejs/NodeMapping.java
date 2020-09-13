package org.marvelousness.springboot.oms.properties.nodejs;

import java.util.List;

import lombok.Data;

/**
 * NodeJs的映射文件
 *
 * @author 981247127@qq.com
 * @time 2020-08-15 15:25
 */
@Data
public class NodeMapping {
	/**
	 * 针对的地址
	 */
	private String href;
	/**
	 * 样式表
	 */
	private List<String> csses;
	/**
	 * 脚本
	 */
	private List<String> jses;
}