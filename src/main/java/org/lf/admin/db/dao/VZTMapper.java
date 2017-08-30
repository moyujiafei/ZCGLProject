package org.lf.admin.db.dao;

import org.lf.admin.db.pojo.VZT;

public interface VZTMapper extends BaseMapper<VZT>{
	
	int countVZTList(VZT param);
}