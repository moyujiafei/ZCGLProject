package org.lf.admin.db.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lf.admin.db.pojo.CZCGL;

public interface BMZC_ZCFLStatMapper {
		
	 ConcurrentHashMap<String, Object> zcflOneByAppidAndDeptNo(Map<String, Object> param);
     
     List<ConcurrentHashMap<String, Object>> selectZCFLOneList(CZCGL zcgl);
     
     List<String> getLxidByLxpid(Integer appId);
     
}
