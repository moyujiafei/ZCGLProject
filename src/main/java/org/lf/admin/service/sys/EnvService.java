package org.lf.admin.service.sys;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.ChuEnvMapper;
import org.lf.admin.db.pojo.ChuEnv;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 环境设置
 * @author 
 */
@Service("envService")
public class EnvService {
	
	@Autowired
	private ChuEnvMapper envDao;
	
	public static final OperErrCode 环境变量名不存在 = new OperErrCode("10001", "环境变量名不存在");
	public static final OperErrCode 环境变量名不能为空 = new OperErrCode("10001", "环境变量名不能为空");
	public static final OperErrCode 环境变量名重复 = new OperErrCode("10001", "环境变量名重复");
	
	public static final String RW_EXPIRE_DATE = "RW_EXPIRE_DATE";
	public static final String ZC_EXPIRE_DATE = "ZC_EXPIRE_DATE";
	public static final String TAG_HQ_ADMIN = "TAG_HQ_ADMIN";
	
	private void checkEnv(ChuEnv env, boolean isInsert) throws OperException {
		if (StringUtils.isEmpty(env.getEnvKey())) {
			throw new OperException(环境变量名不能为空);
		}
		
		// 环境变量名不能重复
		if (isInsert) {
			ChuEnv param = new ChuEnv();
			param.setEnvKey(env.getEnvKey());
			if (countEnvList(param) > 0) {
				throw new OperException(环境变量名重复 );
			}
		}
		
	}
	
	/**
	 * 前端环境名是否重复
	 * @param appId
	 * @param newEnvKey
	 * @return
	 */
	public boolean checkEnvKey(String newEnvKey, String oldEnvKey) {
		if (StringUtils.isEmpty(newEnvKey)) {
			return true;
		}
		
		if (! StringUtils.isEmpty(oldEnvKey) && oldEnvKey.equals(newEnvKey)) {
			return true;
		}
		
		// 校区名称不能重复
		ChuEnv param = new ChuEnv();
		param.setEnvKey(newEnvKey);
		if (countEnvList(param) > 0) {
			return false;
		}
		
		return true;
	}
	
	
	public int countEnvList(ChuEnv param) {
		return envDao.countEnvList(param);
	}
	
	public List<ChuEnv> getEnvList(ChuEnv param) {
		return envDao.getEnvList(param);
	}
	
	/**
	 * 获取指定页的系统环境列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public List<ChuEnv> getEnvList(ChuEnv param,int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return getEnvList(param);
	}
	
	/**
	 * 获取指定页的系统环境列表。
	 * 
	 * @param rows
	 *            一页的记录数
	 * @param page
	 *            当前页号
	 * @return
	 */
	public EasyuiDatagrid<ChuEnv> getPageEnvList(ChuEnv param, int rows, int page) {
		int total = countEnvList(param);

		EasyuiDatagrid<ChuEnv> pageDatas = new EasyuiDatagrid<ChuEnv>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<ChuEnv>());
		} else {
			List<ChuEnv> list = getEnvList(param, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;	
	}
	
	/**
	 * 根据环境主键获取环境信息
	 * @param envKey
	 * @return
	 */
	public ChuEnv getEnv(Integer id) {
		ChuEnv result = envDao.selectByPrimaryKey(id);
		return result;
	}
	
	/**
	 * 根据环境变量名获取环境信息
	 * @param envKey
	 * @return
	 * @throws OperException 环境变量名不存在
	 */
	public ChuEnv getEnv(String envKey) throws OperException {
		ChuEnv param = new ChuEnv();
		param.setEnvKey(envKey);
		
		ChuEnv result = envDao.select(param);
		if (result == null) {
			throw new OperException(环境变量名不存在);
		}
		
		return result;
	}
	
	public String getEnvValue(String envKey) throws OperException {
		return getEnv(envKey).getEnvValue().trim();
	}
	
	/**
	 * 插入一个环境记录
	 * @param record
	 * @throws OperException 环境变量名不能为空， 环境变量名重复，环境变量值不能为空
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertEnv(ChuEnv record) throws OperException {
		checkEnv(record, true);
		envDao.insertSelective(record);
	}
	
	/**
	 * 插入一个环境记录
	 * @param record
	 * @throws OperException 环境变量名不能为空， 环境变量名重复，环境变量值不能为空
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertEnv(String envKey, String envValue) throws OperException {
		ChuEnv record = new ChuEnv();
		record.setEnvKey(envKey);
		record.setEnvValue(envValue);
		insertEnv(record);
	}
	
	/**
	 * 插入一个环境记录
	 * @param record
	 * @throws OperException 环境变量名不能为空， 环境变量名重复，环境变量值不能为空
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateEnv(ChuEnv record) throws OperException {
		checkEnv(record, false);
		envDao.updateByPrimaryKeySelective(record);
	}
	
	/**
	 * 依据变量名，更新相应的变量值、备注信息
	 * @param record
	 * @throws OperException 环境变量名不存在，环境变量值不能为空
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateEnv(Integer id, String envValue, String remark) throws OperException {
		ChuEnv record = getEnv(id);
		record.setEnvValue(envValue);
		record.setRemark(remark);
		updateEnv(record);
	}
}
