package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.CZCGL;

public interface CZCGLMapper extends BaseMapper<CZCGL> {
    int deleteByPrimaryKey(Integer id);

    int insert(CZCGL record);

    int insertSelective(CZCGL record);

    CZCGL selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CZCGL record);

    int updateByPrimaryKey(CZCGL record);

	int countZcglList(CZCGL param);

	List<CZCGL> selectZCGLList(CZCGL param);
	
	@Select("select getWXDeptRootList(#{appId,jdbcType=INTEGER},#{rootId,jdbcType=INTEGER})")
	String getWXDeptRootList(@Param("appId") Integer appId,@Param("rootId") Integer rootId);
	
    @Delete("delete from c_zcgl where app_id = #{appId,jdbcType=INTEGER}")
    int deleteByAppId(@Param("appId") Integer appId);
}