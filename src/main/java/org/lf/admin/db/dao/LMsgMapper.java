package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.LMsg;

public interface LMsgMapper extends BaseMapper<LMsg> {
    int deleteByPrimaryKey(Integer id);

    int insert(LMsg record);

    int insertSelective(LMsg record);

    LMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LMsg record);

    int updateByPrimaryKey(LMsg record);
    
    int countMsgList(LMsg param);
    
    List<LMsg> getMsgList(LMsg param);
    
    List<LMsg> getMsgListByJsr(LMsg param);
}