package org.marvelousness.springboot.oms.entity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Data;

/**
 * 分页实体
 * 
 * @author 981247127@qq.com
 * @time 2020-08-22 16:47
 */
@Data
public class ResponsePageEntity<T> implements Iterable<T>, Serializable {
	private static final long serialVersionUID = -4728248417613921938L;
	/**
	 * 页码数
	 */
	private Integer number = 1;
	/**
	 * 页项数，每页记录条数
	 */
	private Integer size = 10;
	/**
	 * 要显示到页面上的结果集
	 */
	private List<T> rows = null;
	/**
	 * 总记录数
	 */
	private Integer total = 0;

	/**
	 * 用于响应分页输出的对象
	 * 
	 * @param number 页码数
	 * @param size   页项数
	 * @param list   页数据
	 * @param total  记录总数
	 */
	public ResponsePageEntity(List<T> list, Integer total, Integer number, Integer size) {
		this.number = number == null ? 1 : number;
		this.size = size == null ? 1 : size;
		this.rows = list == null ? new CopyOnWriteArrayList<T>() : list;
		this.total = total == null ? 0 : total;
	}

	/**
	 * 实现 Iterable 接口, 可以for(Object item : page)遍历使用
	 */
	public Iterator<T> iterator() {
		return rows.iterator();
	}

	/**
	 * 获取总页数，根据页项数与总记录数计算总页数
	 */

	public Integer getCount() {
		if (this.size == null || this.size < 1) {
			return 0;
		}
		int count = (int) Math.ceil((double) this.total / (double) this.size);
		return count;
	}

	/**
	 * 是否还有下一页
	 */
	public Boolean hasNext() {
		if (this.number == null || getCount() == null) {
			return false;
		}
		return (this.number + 1 <= getCount());
	}

	/**
	 * 是否最后一页
	 */
	public Boolean isLast() {
		return !hasNext();
	}

	/**
	 * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号
	 */
	public Integer getNext() {
		if (this.number == null) {
			this.number = 1;
		}
		if (hasNext()) {
			return this.number + 1;
		} else {
			return this.number;
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public Boolean hasPrev() {
		if (this.number == null) {
			return false;
		}
		return (this.number > 1);
	}

	/**
	 * 是否第一页.
	 */
	public Boolean isFirst() {
		return !hasPrev();
	}

	/**
	 * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
	 */
	public Integer getPrev() {
		if (this.number == null) {
			this.number = 1;
		}
		if (hasPrev()) {
			return this.number - 1;
		} else {
			return this.number;
		}
	}

}