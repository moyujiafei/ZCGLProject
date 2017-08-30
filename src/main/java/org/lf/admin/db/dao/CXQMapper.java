package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.CXQ;

public interface CXQMapper extends BaseMapper<CXQ>{
    int deleteByPrimaryKey(Integer id);

    int insert(CXQ record);

    int insertSelective(CXQ record);

    CXQ selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CXQ record);

    int updateByPrimaryKey(CXQ record);

	int countXQList(CXQ param);
	
}