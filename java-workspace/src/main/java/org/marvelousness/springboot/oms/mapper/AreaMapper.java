package org.marvelousness.springboot.oms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.marvelousness.springboot.oms.entity.pojo.Area;

/**
 * 地区
 * 
 * @author 981247127@qq.com
 * @time 2020-09-05 10:50
 */
public interface AreaMapper {
	public static final String TABLE_NAME = "`oms_area`";

	/**
	 * 查询指定地区下的所有地区
	 * 
	 * @return
	 */
	@Results(id = "Area", value = { @Result(column = "id", property = "id"), @Result(column = "name", property = "name"), @Result(column = "id", property = "areas", many = @Many(select = "org.marvelousness.springboot.oms.mapper.AreaMapper.getAreas")) })
	@Select("SELECT `id`, `name` FROM " + TABLE_NAME + " WHERE `pid` = #{pid}")
	public List<Area> getAreas(Integer pid);
	
	/**
	 * 查询所有的二级城市
	 * @return
	 */
	@Select("SELECT `name` FROM " + TABLE_NAME + " WHERE `pid` <> '0'")
	public List<String> selectSecondAreaNames();
}