package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.VJZW;

public interface VJZWMapper extends BaseMapper<VJZW>{
	// 与selectList配套的
	int countJZWList(VJZW param);
	
	VJZW selectByPrimaryKey(Integer id);
	
	//校区名称，建筑类型，建筑物名称模糊查询
	int countJZWListFuzzy(VJZW param);

	//校区名称，建筑类型，建筑物名称模糊查询
	List<VJZW> getJZWListFuzzy(VJZW param);
}