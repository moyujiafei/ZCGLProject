package org.lf.admin.service.yhpgl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.lf.admin.db.dao.JYHPMapper;
import org.lf.admin.db.dao.JYHPSQMapper;
import org.lf.admin.db.dao.JYHPSQXZMapper;
import org.lf.admin.db.dao.LYHPMapper;
import org.lf.admin.db.pojo.JYHP;
import org.lf.admin.db.pojo.JYHPSQ;
import org.lf.admin.db.pojo.JYHPSQXZ;
import org.lf.admin.db.pojo.LYHP;
import org.lf.admin.db.pojo.VYHPSQXZ;
import org.lf.admin.service.OperException;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 易耗品申请单细则管理
 * 
 */
@Service("yhpSQXZService")
public class YHPSQXZService {
	@Autowired
	private JYHPMapper yhpDao;
	
	@Autowired
	private LYHPMapper lyhpDao;
	
	@Autowired
	private JYHPSQMapper sqDao;
	
	@Autowired
	private JYHPSQXZMapper jsqxzDao;

	public int countYHPSQXZList(Integer appId, String sqdm) {
		return -1;
	}
	
	public List<VYHPSQXZ> getYHPSQXZList(Integer appId, String sqdm, int rows, int page) {
		return null;
	}
	
	public EasyuiDatagrid<VYHPSQXZ> getPagedYHPSQXZList(Integer appId, String sqdm, int rows, int page) {
		return null;
	}
	
	/**
	 * 根据申请代码查询v_yhp_sqxz视图，构建map。key值为yhp_id。
	 * @param sqdm
	 * @return
	 */
	public Map<Integer, VYHPSQXZ> getYHPSQXZList(String sqdm) {
		return null;
	}
	
	public EasyuiDatagrid<VYHPSQXZ> getPagedYHPSQXZList(Integer appId, String sqdm) {
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean insertYHPSQXZ(String sqdm, Map<Integer, Integer> sqNumMap) throws OperException {
		return false;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean delYHPSQXZ(String sqdm) throws OperException {
		return false;
	}
	
	/**
	 * 获取缺省的审批数量值。
	 * spNumMap：<key>为yhp_id，<value>为min(持有量，申请数量)
	 */
	public Map<Integer, Integer> getDefaultSPNum(String sqdm) {
		return null;
	}
	
	/**
	 * 审批数量取值为0 - min(持有量，申请数量)，否则报错。
	 * @throws OperException
	 */
	public boolean checkSPNum(String sqdm, Map<Integer, Integer> spNumMap) throws OperException {
		return false;
	}
	
	/**
	 * 同意易耗品领用申请
	 * 
	 * 1. 审批数量取值为0 - min(持有量，申请数量)
	 * 2. 根据填写的SP_NUM，修改J_YHP_SQXZ相应记录的审批数量（SP_NUM）
	 * 3. 将J_YHP_SQ的status置为同意。
	 * 4. 更新J_YHP中持有数量
	 * 4. 如果申请单是部门申领，则在J_YHP中插入一组记录，用于记录易耗品新的持有部门
	 * 		易耗品类型lx_id：spNumMap的key值（yhp_id）查询获得
	 * 		规格型号xh：spNumMap的key值（yhp_id）查询获得
	 * 		出厂编号ccbh：spNumMap的key值（yhp_id）查询获得
	 * 		持有部门zcgl_id：申请单(sqdm对应记录）中的申请部门
	 * 		持有数量：spNumMap中的value值
	 * 		库存下限left_limit：spNumMap的key值（yhp_id）查询获得
	 * 		pic_url：spNumMap的key值（yhp_id）查询获得
	 * 		img_version: spNumMap的key值（yhp_id）查询获得
	 * 5. 插入一组L_YHP记录
	 * 		记录人jlr：
	 * 		记录时间jlsj：当前系统时间
	 * 		操作人czr：申请单(sqdm对应记录）中的申请人
	 * 		操作部门czbm_id：申请单(sqdm对应记录）中的申请部门
	 * 		操作类型czlx：领用
	 *      易耗品yhp_id：spNumMap的key值
	 * 		数量num：spNumMap中的value值
	 * 
	 * @param appId
	 * @param jlr
	 * @param sqdm
	 * @param spNumMap
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public void agreeLeadingSQ(Integer appId, String jlr, String sqdm, Map<Integer, Integer> spNumMap) throws OperException {
		checkSPNum(sqdm, spNumMap);
		
		Map<Integer, VYHPSQXZ> sqxzMap = getYHPSQXZList(sqdm);
		JYHPSQ sq = sqDao.selectByPrimaryKey(sqdm);
		
		Integer spNum;
		VYHPSQXZ vsqxz;
		JYHPSQXZ jsqxz;
		JYHP yhp, newYhp, yhpParam;
		LYHP lyhp;
		for (Integer yhpId : spNumMap.keySet()) {
			spNum = spNumMap.get(yhpId);
			// 根据填写的SP_NUM，修改J_YHP_SQXZ相应记录的审批数量（SP_NUM）
			vsqxz = sqxzMap.get(yhpId);
			jsqxz = jsqxzDao.selectByPrimaryKey(vsqxz.getXzid());
			jsqxz.setSpNum(spNum);
			jsqxzDao.updateByPrimaryKey(jsqxz);
			
			// 更新J_YHP中持有数量
			yhp = yhpDao.selectByPrimaryKey(yhpId);
			yhp.setNum(yhp.getNum() - spNum);
			yhpDao.updateByPrimaryKey(yhp);
			
			// 如果申请单是部门申领，则在J_YHP中插入一组记录，用于记录易耗品新的持有部门
			if (sq.getSqType().equals(YHPSQType.部门申领.getValue())) {
				yhpParam = new JYHP();
				yhpParam.setAppId(appId);
				yhpParam.setLxId(yhp.getLxId());
				yhpParam.setXh(yhp.getXh());
				yhpParam.setCcbh(yhp.getCcbh());
				// 申请单(sqdm对应记录）中的申请部门
				yhpParam.setZcglId(sq.getSqbmId());
				newYhp = yhpDao.select(yhpParam);
				
				// 依据appId, lx_id, xh, ccbh， zcglId查询现有j_yhp中是否存在相关记录
				if (newYhp == null) {
					// 如果不存在记录，创建新的易耗品持有记录
					newYhp = yhpParam;
					newYhp.setNum(spNum);
					newYhp.setCcbh(null);
					newYhp.setLeftLimit(yhp.getLeftLimit());
					newYhp.setPicUrl(yhp.getPicUrl());
					newYhp.setImgVersion(yhp.getImgVersion());
					yhpDao.insertSelective(newYhp);
				} else {
					// 如果存在记录，则在原有记录基础上，累加持有数量。
					newYhp.setNum(newYhp.getNum() + spNum);
					yhpDao.updateByPrimaryKey(newYhp);
				}				
			}
			
			// 插入L_YHP记录
			lyhp = new LYHP();
			lyhp.setYhpId(yhpId);
			lyhp.setJlr(jlr);
			lyhp.setJlsj(new Date());
			lyhp.setCzr(sq.getSqr());
			lyhp.setCzbmId(sq.getSqbmId());
			lyhp.setCzlx(YHPCZLX.领用.getValue());
			lyhp.setNum(spNum);
			lyhpDao.insertSelective(lyhp);
		}
		
		// 将J_YHP_SQ的status置为同意。
		sq.setStatus(YHPSQStatus.同意.getValue());
		sqDao.updateByPrimaryKey(sq);
	}
	
	/**
	 * 同意易耗品报损申请
	 * 
	 * 1. 审批数量取值为0 - min(持有量，申请数量)
	 * 2. 根据填写的SP_NUM，修改J_YHP_SQXZ相应记录的审批数量（SP_NUM）
	 * 3. 将J_YHP_SQ的status置为同意。
	 * 4. 更新J_YHP中的持有量
	 * 5. 插入一组L_YHP记录
	 * 		记录人jlr：
	 * 		记录时间jlsj：当前系统时间
	 * 		操作人czr：申请单(sqdm对应记录）中的申请人
	 * 		操作部门czbm_id：申请单(sqdm对应记录）中的申请部门
	 * 		操作类型czlx：报损
	 *      易耗品yhp_id：spNumMap的key值
	 * 		数量num：spNumMap中的value值
	 * 
	 * @param appId
	 * @param jlr
	 * @param sqdm
	 * @param spNumMap
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public void agreeLossingSQ(Integer appId, String jlr, String sqdm, Map<Integer, Integer> spNumMap) throws OperException {
		checkSPNum(sqdm, spNumMap);
		
		Map<Integer, VYHPSQXZ> sqxzMap = getYHPSQXZList(sqdm);
		JYHPSQ sq = sqDao.selectByPrimaryKey(sqdm);
		
		Integer spNum;
		VYHPSQXZ vsqxz;
		JYHPSQXZ jsqxz;
		JYHP yhp;
		LYHP lyhp;
		for (Integer yhpId : spNumMap.keySet()) {
			spNum = spNumMap.get(yhpId);
			// 根据填写的SP_NUM，修改J_YHP_SQXZ相应记录的审批数量（SP_NUM）
			vsqxz = sqxzMap.get(yhpId);
			jsqxz = jsqxzDao.selectByPrimaryKey(vsqxz.getXzid());
			jsqxz.setSpNum(spNum);
			jsqxzDao.updateByPrimaryKey(jsqxz);
			
			// 更新J_YHP中持有数量
			yhp = yhpDao.selectByPrimaryKey(yhpId);
			yhp.setNum(yhp.getNum() - spNum);
			yhpDao.updateByPrimaryKey(yhp);
			
			// 插入L_YHP记录
			lyhp = new LYHP();
			lyhp.setYhpId(yhpId);
			lyhp.setJlr(jlr);
			lyhp.setJlsj(new Date());
			lyhp.setCzr(sq.getSqr());
			lyhp.setCzbmId(sq.getSqbmId());
			lyhp.setCzlx(YHPCZLX.报损.getValue());
			lyhp.setNum(spNum);
			lyhpDao.insertSelective(lyhp);
		}
		
		// 将J_YHP_SQ的status置为同意。
		sq.setStatus(YHPSQStatus.同意.getValue());
		sqDao.updateByPrimaryKey(sq);
	}
}
