package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.CFJ;

public interface CFJMapper extends BaseMapper<CFJ>{
    int deleteByPrimaryKey(Integer id);

    int insert(CFJ record);

    int insertSelective(CFJ record);

    CFJ selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CFJ record);

    int updateByPrimaryKey(CFJ record);
    
    int countFJList(CFJ param);

	List<String> selectFloorList(CFJ param);
}