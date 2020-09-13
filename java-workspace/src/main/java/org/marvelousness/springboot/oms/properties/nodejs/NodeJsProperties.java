package org.marvelousness.springboot.oms.properties.nodejs;

import java.util.List;

import org.marvelousness.springboot.basic.utils.StringUtils;

import lombok.Data;

/**
 * nodejs 前端配置
 *
 * @author 981247127@qq.com
 * @time 2020-08-15 15:25
 */
@Data
public class NodeJsProperties {
	/**
	 * NodeJs 服务端地址
	 */
	private String server;
	/**
	 * 映射地址的集合
	 */
	private List<NodeMapping> mappings;

	/**
	 * 获取根目录的，默认第一个问根目录的node映射
	 * 
	 * @return
	 */
	public NodeMapping getNodeMapping() {
		return getNodeMapping(null);
	}

	/**
	 * 获取指定的node映射
	 * 
	 * @param href
	 * @return
	 */
	public NodeMapping getNodeMapping(String href) {
		if (StringUtils.isBlank(href) && mappings != null && mappings.size() == 1) {
			return mappings.get(0);
		}
		if (mappings == null || mappings.isEmpty()) {
			return null;
		}
		for (NodeMapping mapping : mappings) {
			if (mapping == null || StringUtils.isBlank(mapping.getHref())) {
				continue;
			}
			if (mapping.getHref().equals(href)) {
				return mapping;
			}
		}
		return null;
	}
}