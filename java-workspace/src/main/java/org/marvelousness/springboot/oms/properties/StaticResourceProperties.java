package org.marvelousness.springboot.oms.properties;

import java.util.List;

import org.marvelousness.springboot.oms.properties.nodejs.NodeMapping;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 静态资源服务器的属性，该类是控制页面到底该引用哪里的 JS 资源的依据
 *
 * @author 981247127@qq.com
 * @time 2020-08-15 15:24
 */
@Data
@Component
@ConfigurationProperties(prefix = "static-resource")
public class StaticResourceProperties {
	/**
	 * 资源服务站地址
	 */
	private String server;
	/**
	 * 显示在 HTML 页面中 title 标签中表示当前系统名称的文本
	 */
	private String title;
	/**
	 * 表示当前系统的版本号
	 */
	private String version;
	/**
	 * 允许被匿名访问的路径
	 */
	private String[] anons;
	/**
	 * 当处于调试模式时，映射地址的集合
	 */
	private List<NodeMapping> debugMappings;
	/**
	 * 当处于发布模式时，映射地址的集合
	 */
	private List<NodeMapping> releaseMappings;
}