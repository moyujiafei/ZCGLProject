package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.LYHPXZ;

public interface LYHPXZMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LYHPXZ record);

    int insertSelective(LYHPXZ record);

    LYHPXZ selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LYHPXZ record);

    int updateByPrimaryKey(LYHPXZ record);
}