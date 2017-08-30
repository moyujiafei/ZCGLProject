package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.LZT;

public interface LZTMapper extends BaseMapper<LZT> {
    int deleteByPrimaryKey(Integer id);

    int insert(LZT record);

    int insertSelective(LZT record);

    LZT selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LZT record);

    int updateByPrimaryKey(LZT record);
}