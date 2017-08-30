package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.VLYHP;

public interface VLYHPMapper {
    int insert(VLYHP record);

    int insertSelective(VLYHP record);
}