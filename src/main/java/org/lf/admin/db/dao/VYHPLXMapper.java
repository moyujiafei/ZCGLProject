package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.VYHPLX;

public interface VYHPLXMapper {
    int insert(VYHPLX record);

    int insertSelective(VYHPLX record);

	int countLevel1YHPLXList(VYHPLX param);

	int countLevel2YHPLXList(VYHPLX param);

	List<VYHPLX> selectLevel1YHPLXList(VYHPLX param);

	List<VYHPLX> selectLevel2YHPLXList(VYHPLX param);

	int countYHPLXList(VYHPLX param);


}