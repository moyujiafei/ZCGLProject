package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.JYHP;

public interface JYHPMapper extends BaseMapper<JYHP> {
    int deleteByPrimaryKey(Integer id);

    int insert(JYHP record);

    int insertSelective(JYHP record);

    JYHP selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JYHP record);

    int updateByPrimaryKey(JYHP record);
}