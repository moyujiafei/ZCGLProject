package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.VYHP;

public interface VYHPMapper {
    int insert(VYHP record);

    int insertSelective(VYHP record);
}