package org.lf.admin.service.yhpgl;

import java.util.List;
import java.util.Map;

import org.lf.admin.db.pojo.JYHPSQ;
import org.lf.admin.service.OperException;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 易耗品申请单管理：包括申领单和报损单
 * 
 */
@Service("yhpSQService")
public class YHPSQService {
	@Autowired
	private YHPSQXZService sqxzService;

	public int countYHPSQList(Integer appId, YHPSQType type, String sqr, YHPSQStatus status) {
		return -1;
	}
	
	public List<JYHPSQ> getYHPSQList(Integer appId, YHPSQType type, String sqr, YHPSQStatus status, int rows, int page) {
		return null;
	}
	
	public EasyuiDatagrid<JYHPSQ> getPagedYHPSQList(Integer appId, YHPSQType type, String sqr, YHPSQStatus status, int rows, int page) {
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	public void insertYHPSQ(Integer appId, String sqr, YHPSQType type, Integer sqbmId, Map<Integer, Integer> sqNumMap) throws OperException {

	}
	
	@Transactional(rollbackFor = Exception.class)
	public void updateYHPSQ(String sqdm, Map<Integer, Integer> sqNumMap) throws OperException {
		sqxzService.delYHPSQXZ(sqdm);
		sqxzService.insertYHPSQXZ(sqdm, sqNumMap);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void delYHPSQ(String sqdm) throws OperException {
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void submitYHPSQ(String sqdm) throws OperException {
		
	}
}
