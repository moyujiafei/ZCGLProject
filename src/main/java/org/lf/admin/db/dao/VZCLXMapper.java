package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.VZCLX;

public interface VZCLXMapper extends BaseMapper<VZCLX>{

	int countZCLXList(VZCLX param);
	
	List<VZCLX> selectZCLXList(VZCLX param);

	VZCLX selectByPrimaryKey(Integer id);

	List<VZCLX> selectLevel1ZCLXList(VZCLX param);

	List<VZCLX> selectLevel2ZCLXList(VZCLX param);
	
	int countLevel1ZCLXList(VZCLX param);

	int countLevel2ZCLXList(VZCLX param);

	
}