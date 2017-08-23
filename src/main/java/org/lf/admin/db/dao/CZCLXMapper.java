package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.CZCLX;

public interface CZCLXMapper extends BaseMapper<CZCLX> {
    int deleteByPrimaryKey(Integer id);

    int insert(CZCLX record);

    int insertSelective(CZCLX record);

    CZCLX selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CZCLX record);

    int updateByPrimaryKey(CZCLX record);

	List<CZCLX> selectLevel1List(CZCLX param);
	
	List<CZCLX> selectLevel2List(CZCLX param);
}