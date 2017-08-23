package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.ChuMenu;

public interface ChuMenuMapper extends BaseMapper<ChuMenu> {
    int deleteByPrimaryKey(Integer id);

    int insert(ChuMenu record);

    int insertSelective(ChuMenu record);

    ChuMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChuMenu record);

    int updateByPrimaryKey(ChuMenu record);

	@Select("SELECT m.* FROM chu_menu m INNER JOIN chu_role_menu rm ON (m.id = rm.menu_id AND rm.role_id = #{roleId,jdbcType=INTEGER})")
	@ResultMap("BaseResultMap")
	List<ChuMenu> selectMenuList(@Param("roleId")Integer roleId);
}