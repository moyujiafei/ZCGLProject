package org.lf.admin.db.dao;

import java.util.List;
import java.util.Map;

import org.lf.admin.db.pojo.BMZC_GZSJStat;
import org.lf.admin.db.pojo.VZC;

public interface BMZC_GZSJStatMapper {
	
	BMZC_GZSJStat gzsjByAppidAndDeptNo (Map<String, Object> param);		 
	
	List<BMZC_GZSJStat> selectList (VZC zc);
	
}
