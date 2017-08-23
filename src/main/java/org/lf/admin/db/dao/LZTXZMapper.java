package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.LZTXZ;

public interface LZTXZMapper extends BaseMapper<LZTXZ> {
    int deleteByPrimaryKey(Integer id);

    int insert(LZTXZ record);

    int insertSelective(LZTXZ record);

    LZTXZ selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LZTXZ record);

    int updateByPrimaryKey(LZTXZ record);

    @Select("SELECT xz.*,zt.JLSJ FROM l_ztxz xz, l_zt zt WHERE xz.JL_ID = zt.ID"
    		+ " AND xz.JL_ID = #{ztid,jdbcType=INTEGER} AND xz.MEDIA_TYPE = #{mediaType,jdbcType=VARCHAR}")
    @ResultMap("BaseResultMap")
	List<LZTXZ> selectListByZt(@Param("ztid")Integer ztid, @Param("mediaType")String mediaType);
}