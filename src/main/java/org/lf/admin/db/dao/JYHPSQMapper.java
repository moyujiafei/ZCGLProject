package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.JYHPSQ;

public interface JYHPSQMapper {
    int deleteByPrimaryKey(String dm);

    int insert(JYHPSQ record);

    int insertSelective(JYHPSQ record);

    JYHPSQ selectByPrimaryKey(String dm);

    int updateByPrimaryKeySelective(JYHPSQ record);

    int updateByPrimaryKey(JYHPSQ record);
}