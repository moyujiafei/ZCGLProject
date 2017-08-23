package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.CJZW;

public interface CJZWMapper extends BaseMapper<CJZW>{
    int deleteByPrimaryKey(Integer id);

    int insert(CJZW record);

    int insertSelective(CJZW record);

    CJZW selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CJZW record);

    int updateByPrimaryKey(CJZW record);
}