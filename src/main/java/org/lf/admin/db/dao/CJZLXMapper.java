package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.CJZLX;

public interface CJZLXMapper extends BaseMapper<CJZLX>{
    int deleteByPrimaryKey(Integer id);

    int insert(CJZLX record);

    int insertSelective(CJZLX record);

    CJZLX selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CJZLX record);

    int updateByPrimaryKey(CJZLX record);
    
    int countJZLXList(CJZLX param);
    
    List<CJZLX> getJZLXList(CJZLX param);
}