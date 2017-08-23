package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.LYHP;

public interface LYHPMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LYHP record);

    int insertSelective(LYHP record);

    LYHP selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LYHP record);

    int updateByPrimaryKey(LYHP record);
}