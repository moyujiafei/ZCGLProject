package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.db.pojo.WxDept;

public interface ChuWXDeptMapper extends BaseMapper<ChuWXDept> {
    int deleteByPrimaryKey(Integer id);

    int insert(ChuWXDept record);

    int insertSelective(ChuWXDept record);

    ChuWXDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChuWXDept record);

    int updateByPrimaryKey(ChuWXDept record);
    
    int insertAll(List<ChuWXDept> wxDeptList);
    
    @Delete("delete from chu_wxdept where app_id = #{appId,jdbcType=INTEGER}")
    int deleteByAppId(@Param("appId") Integer appId);
    
    int countDeptList(ChuWXDept param);
    
    //获取指定部门的所有子部门字符串（其中包括指定部门）
    @Select("select getWXDeptList(#{appId,jdbcType=INTEGER},#{deptno,jdbcType=INTEGER})")
    String getSubDeptStrByDeptNo(@Param("appId")Integer appId,@Param("deptno")Integer deptno);
    
    // 用于微信端显示三级部门信息。例如：我的通讯录
	List<WxDept> getWxDeptsLevel(Integer appId);
}