package org.lf.admin.service.yhpgl;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.VYHPMapper;
import org.lf.admin.db.pojo.JYHP;
import org.lf.admin.db.pojo.VYHP;
import org.lf.admin.service.OperException;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 易耗品管理
 * 
 */
@Service("yhpService")
public class YHPService {
	@Autowired
	private VYHPMapper vyhpDao;
	
	public int countYHPList(Integer appId, String lx, String fzr) {
		VYHP param=new VYHP();
		param.setAppId(appId);
		param.setLx(lx);
		param.setFzr(fzr);
		return vyhpDao.countYhpList(param);
	}
	
	public List<VYHP> getYHPList(Integer appId, String lx, String fzr, int rows, int page) {
		return null;
	}
	
	public EasyuiDatagrid<VYHP> getPagedYHPList(Integer appId, String lx, String fzr, int rows, int page) {
		int total=countYHPList(appId, lx, fzr);
		EasyuiDatagrid<VYHP> result=new EasyuiDatagrid<>();
		if(total>0){
			VYHP param=new VYHP();
			param.setAppId(appId);
			param.setLx(lx);
			param.setFzr(fzr);
			PageNavigator pageNav=new PageNavigator(rows, page);
			param.setStart(pageNav.getStart());
			param.setOffset(pageNav.getOffset());
			result.setRows(vyhpDao.selectYhpList(param));
		}else{
			result.setRows(new ArrayList<VYHP>());
		}
		result.setTotal(total);
		return result;
	}
	
	/**
	 * 企业易耗品登记，czbmId为空；部门易耗品登记，操作部门为用户所在部门id号。
	 * select id from c_zcgl where appid=? and fzr=?
	 * 这里，负责人（fzr）为当前用户userid。
	 *  
	 * 
	 * 	向J_YHP表中插入一条记录。
	 * 	向L_YHP表中插入一条记录：
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertYHP(Integer appId, String jlr, Integer czbmId, Integer lxId, String picUrl, String xh, String ccbh, Integer num,
			Integer leftLimit, String cfdd) throws OperException {

	}
	
	@Transactional(rollbackFor = Exception.class)
	public void updateYHP(Integer yhpId, String picUrl, String xh, String ccbh, Integer leftLimit, String cfdd) throws OperException {
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void delYHP(Integer yhpId) throws OperException {
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void addYHP(Integer yhpId, Integer addNum) throws OperException {
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void allocateYHP(Integer yhpId, Integer allocateZCGLId, Integer allocateNum, String cfdd) throws OperException {
		
	}

	public JYHP getYHP(Integer id) {
		return null;
	}
}
