package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.ChuApp;

public interface ChuAppMapper extends BaseMapper<ChuApp> {
    int deleteByPrimaryKey(Integer appId);

    int insert(ChuApp record);

    int insertSelective(ChuApp record);

    ChuApp selectByPrimaryKey(Integer appId);

    int updateByPrimaryKeySelective(ChuApp record);

    int updateByPrimaryKey(ChuApp record);
    
	int countAppList(ChuApp param);
	
	@Select("select * from chu_app limit #{start,jdbcType=INTEGER}, #{offset,jdbcType=INTEGER}")
	@ResultMap(value="BaseResultMap")
	List<ChuApp> selectAppList(@Param("start")Integer start, @Param("offset")Integer offset);
}