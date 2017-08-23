package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.ChuTag;

public interface ChuTagMapper extends BaseMapper<ChuTag> {
    int deleteByPrimaryKey(Integer id);

    int insert(ChuTag record);

    int insertSelective(ChuTag record);

    ChuTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChuTag record);

    int updateByPrimaryKey(ChuTag record);
    
    int insertAll(List<ChuTag> tagList);
    
    @Delete("delete from chu_tag where app_id = #{appId,jdbcType=INTEGER}")
    int deleteByAppId(@Param("appId") Integer appId);
    
    /**
     * 根据tagNo和appId查询标签名称
     * @param tagNo
     * @param appId
     * @return
     */
    @Select("SELECT TAG_NAME FROM chu_tag WHERE TAG_NO = #{tagNo,jdbcType=INTEGER} AND APP_ID = #{appId,jdbcType=INTEGER}")
    String getName(@Param("tagNo")Integer tagNo, @Param("appId")Integer appId);
}