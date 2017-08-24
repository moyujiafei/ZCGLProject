package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.VYHP;

public interface VYHPMapper {
    int insert(VYHP record);

    int insertSelective(VYHP record);
    
    int countYhpList(VYHP param);
    
    List<VYHP> selectYhpList(VYHP param);
}