package org.lf.admin.db.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.ChuWXUser;

public interface ChuWXUserMapper extends BaseMapper<ChuWXUser> {
	int deleteByPrimaryKey(Integer id);

	int insert(ChuWXUser record);

	int insertSelective(ChuWXUser record);

	ChuWXUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ChuWXUser record);

	int updateByPrimaryKey(ChuWXUser record);

	int insertAll(List<ChuWXUser> wxUserList);

	int countWXUserList(ChuWXUser param);

	@Delete("delete from chu_wxuser where app_id = #{appId,jdbcType=INTEGER}")
	int deleteByAppId(@Param("appId") Integer appId);

	List<ChuWXUser> selectUserList(ChuWXUser param);
	

	/**
	 * 根据userid和appid查询用户名称
	 * 
	 * @param params
	 * @return
	 */
	@Select("SELECT NAME FROM chu_wxuser WHERE USERID = #{userid,jdbcType=VARCHAR} AND APP_ID = #{appid,jdbcType=INTEGER}")
	String getNameByUseridAndAppid(Map<String, Object> params);

}