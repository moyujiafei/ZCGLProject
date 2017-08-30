package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.ChuUser;

public interface ChuUserMapper extends BaseMapper<ChuUser> {
	int deleteByPrimaryKey(Integer uid);

    int insert(ChuUser record);

    int insertSelective(ChuUser record);

    ChuUser selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(ChuUser record);

    int updateByPrimaryKey(ChuUser record);
	
	int countUserList(ChuUser params);
	
	@Select("select cu.*,cr.NAME ROLE_NAME,app.CORP_NAME CORP_NAME from chu_user cu,chu_role cr,chu_app app WHERE cu.ROLE_ID = cr.ID AND cu.APP_ID = app.APP_ID "
			+ " limit #{start,jdbcType=INTEGER}, #{offset,jdbcType=INTEGER}")
	@ResultMap(value="BaseResultMap")
	List<ChuUser> selectUserList(@Param("start")Integer start, @Param("offset")Integer offset);
	
	/**
	 * 查询用户所有信息包含“角色名称"
	 * @param params
	 * @return
	 */
	ChuUser selectAllInfo(ChuUser params);
	
	/**
	 * 超级管理员appid必须为空
	 * @param uname
	 * @return
	 */
	@Select("select cu.*,cr.NAME ROLE_NAME from chu_user cu,chu_role cr WHERE cu.ROLE_ID = cr.ID AND cu.UNAME = #{uname,jdbcType=VARCHAR}"
			+ " AND cu.APP_ID IS NULL")
	@ResultMap("BaseResultMap")
	ChuUser selectSuperAdminInfo(@Param("uname")String uname);
}