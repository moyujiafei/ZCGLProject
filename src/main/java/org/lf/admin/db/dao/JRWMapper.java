package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.JRW;

public interface JRWMapper extends BaseMapper<JRW>{
    int deleteByPrimaryKey(Integer id);
    
    List<JRW> selectCurRWList(JRW param);
    
    int countRWList(JRW param);
    
    int countCurRWList(JRW param);

    int insert(JRW record);

    int insertSelective(JRW record);

    JRW selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JRW record);

    int updateByPrimaryKey(JRW record);
    
    @Select("select *  from j_rw where app_id = #{appId,jdbcType=INTEGER} and finish = 0   and datediff(jssj, now()) between 0 and (select env_value from chu_env where env_key = 'RW_EXPIRE_DATE')")
    @ResultMap(value="BaseResultMap")
	List<JRW> getExpiringRWList(@Param("appId")Integer appId);
    
    @Select("select * from j_rw where app_id = #{appId,jdbcType=INTEGER} and finish = 0 and jssj < now() ")
    @ResultMap(value="BaseResultMap")
	List<JRW> getExpiredRWList(@Param("appId")Integer appId);
}