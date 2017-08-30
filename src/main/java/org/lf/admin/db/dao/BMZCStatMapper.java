package org.lf.admin.db.dao;

import java.util.Map;

import org.lf.admin.db.pojo.BMZC_ZTStat;

public interface BMZCStatMapper{
	
	BMZC_ZTStat statByAppidAndDeptNo (Map<String, Object> param);		
	
}
