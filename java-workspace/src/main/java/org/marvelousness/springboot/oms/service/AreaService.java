package org.marvelousness.springboot.oms.service;

import java.util.List;

import org.marvelousness.springboot.oms.entity.pojo.Area;
import org.marvelousness.springboot.oms.mapper.AreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author 981247127@qq.com
 * @time 2020-09-05 10:56
 */
@Service
public class AreaService {
	@Autowired
	private AreaMapper mapper;
	
	/**
	 * 查询地区树
	 * @return
	 */
	public List<Area> getAreas() {
		return mapper.getAreas(0);
	}
}