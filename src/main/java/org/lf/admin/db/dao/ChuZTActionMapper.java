package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.ChuZTAction;

public interface ChuZTActionMapper extends BaseMapper<ChuZTAction> {
    int deleteByPrimaryKey(Integer id);

    int insert(ChuZTAction record);

    int insertSelective(ChuZTAction record);

    ChuZTAction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChuZTAction record);

    int updateByPrimaryKey(ChuZTAction record);

	int countChuZTActionList(ChuZTAction param);

	void deleteByAppID(Integer appId);
}