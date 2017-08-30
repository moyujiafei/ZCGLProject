package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.ChuRole;
import org.lf.admin.db.pojo.ChuRoleMenu;

public interface ChuRoleMapper extends BaseMapper<ChuRole> {
    int deleteByPrimaryKey(Integer id);

    int insert(ChuRole record);

    int insertSelective(ChuRole record);

    ChuRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChuRole record);

    int updateByPrimaryKey(ChuRole record);
    
    @Select("select m.id id, m.name text, m.icon_class iconCls, m.pid pid, case when rm.role_id is null then 0 else 1 end checked from chu_menu m left outer join chu_role_menu rm on (m.id = rm.menu_id and rm.role_id = #{roleId, jdbcType=INTEGER}) ")
    @ResultMap("BaseResultMap2")
    List<ChuRoleMenu> selectByRoleId(Integer roleId);
    
	int countRoleList(ChuRole params);
	
	@Select("select r.*,group_concat(m.name) PRIV_LIST from chu_role r, chu_role_menu rm, chu_menu m where rm.role_id = r.id and rm.menu_id = m.id and m.menu_level = 2 group by r.id,r.name limit #{start,jdbcType=INTEGER}, #{offset,jdbcType=INTEGER}")
	@ResultMap(value="BaseResultMap")
	List<ChuRole> selectRoleList(@Param("start")Integer start, @Param("offset")Integer offset);
	
	@Delete("delete from chu_role_menu where role_id = #{roleId,jdbcType=INTEGER}")
	int deleteMenuList(@Param("roleId")Integer roleId);
	
	@Insert("insert into chu_role_menu (role_id, menu_id) values (#{roleId,jdbcType=INTEGER}, #{menuId,jdbcType=INTEGER})")
	int insertMenu(@Param("roleId")Integer roleId, @Param("menuId")Integer menuId);
	
	@Select("select menu_id from chu_role_menu where role_id = #{roleId,jdbcType=INTEGER}")
	List<Integer> selectMenuList(@Param("roleId")Integer roleId);

	
}