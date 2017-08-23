package org.lf.admin.db.dao;

import java.util.List;

import org.lf.admin.db.pojo.VFJ;

public interface VFJMapper extends BaseMapper<VFJ>{
	
	
	int countVFJList(VFJ param);
	
	//根据建筑物名称进行模糊查找
	List<VFJ> getFJListFuzzy(VFJ param);
	
	//根据建筑物名称进行模糊查找
	int countVFJListFuzzy(VFJ param);
	
	//查找param下不重复的floor
	List<String> selectFloorList(VFJ param);
}