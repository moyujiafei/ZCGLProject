package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.JYHPSQXZ;

public interface JYHPSQXZMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JYHPSQXZ record);

    int insertSelective(JYHPSQXZ record);

    JYHPSQXZ selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JYHPSQXZ record);

    int updateByPrimaryKey(JYHPSQXZ record);
}