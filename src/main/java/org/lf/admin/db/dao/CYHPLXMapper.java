package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.CYHPLX;

public interface CYHPLXMapper extends BaseMapper<CYHPLX>{
    int deleteByPrimaryKey(Integer id);

    int insert(CYHPLX record);

    int insertSelective(CYHPLX record);

    CYHPLX selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CYHPLX record);

    int updateByPrimaryKey(CYHPLX record);

	List<CYHPLX> selectLevel1List(CYHPLX param);

	List<CYHPLX> selectLevel2List(CYHPLX param);

}