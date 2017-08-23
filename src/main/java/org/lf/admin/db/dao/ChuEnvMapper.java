package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.ChuEnv;

public interface ChuEnvMapper extends BaseMapper<ChuEnv> {
    int deleteByPrimaryKey(Integer id);

    int insert(ChuEnv record);

    int insertSelective(ChuEnv record);

    ChuEnv selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChuEnv record);

    int updateByPrimaryKey(ChuEnv record);
    
    int countEnvList(ChuEnv param); 
    
    List<ChuEnv> getEnvList(ChuEnv param);
}