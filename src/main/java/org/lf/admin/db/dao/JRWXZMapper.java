package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.JRWXZ;

public interface JRWXZMapper extends BaseMapper<JRWXZ> {
    int deleteByPrimaryKey(Integer id);

    int insert(JRWXZ record);

    int insertSelective(JRWXZ record);

    JRWXZ selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JRWXZ record);

    int updateByPrimaryKey(JRWXZ record);
}