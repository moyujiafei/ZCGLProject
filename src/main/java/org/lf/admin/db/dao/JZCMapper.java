package org.lf.admin.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.lf.admin.db.pojo.JZC;

public interface JZCMapper extends BaseMapper<JZC> {
    int deleteByPrimaryKey(Integer id);

    int insert(JZC record);

    int insertSelective(JZC record);

    JZC selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JZC record);

    int updateByPrimaryKey(JZC record);

	int countZCList(JZC param);
	
	@Select(" select zc.* from j_zc zc join j_rwxz rwxz on (zc.id = rwxz.zcid) where rwxz.rwid = #{rwid,jdbcType=INTEGER}")
	@ResultMap(value="BaseResultMap")
	List<JZC> getZCList(@Param("rwid") Integer rwid);

}