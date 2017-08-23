package org.lf.admin.service.catalog;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.CXQMapper;
import org.lf.admin.db.pojo.CXQ;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 校区管理
 * 
 * @author 石小平
 */
@Service("xqService")
public class XQService {
	@Autowired
	private CXQMapper xqDao;
	
	public static final OperErrCode 校区名称不能为空 = new OperErrCode("10201", "校区名称不能为空");
	public static final OperErrCode 校区名称不能重复 = new OperErrCode("10201", "校区名称不能重复");
	public static final OperErrCode 无法删除有建筑物的校区 = new OperErrCode("10201", "无法删除有建筑物的校区");
	public static final OperErrCode 校区记录不存在 = new OperErrCode("10201", "校区记录不存在");
	
	/**
	 * 这是一个公共方法，用于检查要插入的格式是否正确。
	 * 如果line为空，则不显示“XXXX行”信息。
	 * 
	 * @exception 校区名称不能为空, 校区名称不能重复
	 * 
	 * 校区名称不能重复：指的是在指定的app_Id下，校区名称不能重复。
	 */
	public void checkXQ(CXQ xq, boolean isInsert) throws OperException {
		if (StringUtils.isEmpty(xq.getXqmc())) {
			throw new OperException(校区名称不能为空);
		}
		
		if (isInsert) {
			// 校区名称不能重复
			CXQ param = new CXQ();
			param.setAppId(xq.getAppId());
			param.setXqmc(xq.getXqmc());
			if (xqDao.countXQList(param) > 0) {
				throw new OperException(校区名称不能重复);
			}
		}
	}
	
	/**
	 * 前端验证校区名是否重复
	 * @param appId
	 * @param newXQMC
	 * @return
	 */
	public boolean checkXQMC(Integer appId, String newXQMC) {
		if (StringUtils.isEmpty(newXQMC)) {
			return true;
		}
		
		// 校区名称不能重复
		CXQ param = new CXQ();
		param.setAppId(appId);
		param.setXqmc(newXQMC);
		if (xqDao.countXQList(param) > 0) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 前端验证校区名是否重复
	 * @param appId
	 * @param newXQMC
	 * @return
	 */
	public boolean checkXQMC(Integer appId, String newXQMC, String oldXQMC) {
		if (StringUtils.isEmpty(newXQMC)) {
			return true;
		}
		
		if (oldXQMC.equals(newXQMC)) {
			return true;
		}
		
		// 校区名称不能重复
		CXQ param = new CXQ();
		param.setAppId(appId);
		param.setXqmc(newXQMC);
		if (xqDao.countXQList(param) > 0) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 插入一个校区记录。
	 * 注意：同一个appId下的校区名称不能重复
	 * 
	 * @param xq
	 * @throws OperException 校区名称不能为空, 校区名称不能重复
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertXQ(CXQ xq) throws OperException {
		checkXQ(xq, true);
		xqDao.insertSelective(xq);
	}
	
	/**
	 * 插入一个校区记录。
	 * 注意：同一个appId下的校区名称不能重复
	 * 
	 * @param xq
	 * @throws OperException 校区名称不能为空, 校区名称不能重复
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertXQ(Integer appId, String xqmc, String xqdz, String yb) throws OperException {
		CXQ xq = new CXQ();
		xq.setAppId(appId);
		xq.setXqmc(xqmc);
		xq.setXqdz(xqdz);
		xq.setYb(yb);
		insertXQ(xq);
	}
	
	/**
	 * 更新一个校区记录。
	 * 
	 * @param xq
	 * @throws OperException 校区名称不能为空, 校区名称不能重复
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateXQ(CXQ xq) throws OperException {
		checkXQ(xq, false);
		xqDao.updateByPrimaryKeySelective(xq);
	}
	
	/**
	 * 更新一个校区记录。
	 * 
	 * @param xq
	 * @throws OperException 校区名称不能为空, 校区名称不能重复
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateXQ(Integer xqid, String xqmc, String xqdz, String yb) throws OperException {
		CXQ oldXQ = xqDao.selectByPrimaryKey(xqid);

		CXQ newXQ = new CXQ();
		newXQ.setAppId(oldXQ.getAppId());
		newXQ.setId(xqid);
		newXQ.setXqdz(xqdz);
		newXQ.setYb(yb);
		newXQ.setXqmc(xqmc);
		if (oldXQ.getXqmc().equals(xqmc)) {
			checkXQ(newXQ, false);
			xqDao.updateByPrimaryKeySelective(newXQ);
		} else {
			checkXQ(newXQ, true);
			xqDao.updateByPrimaryKeySelective(newXQ);
		}
	}
	
	/**
	 * 删除校区。
	 * 当校区中存在下级建筑物信息时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param xq
	 * @throws OperException 无法删除有资产的部门
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delXQ(CXQ xq) throws OperException {
		try {
			xqDao.deleteByPrimaryKey(xq.getId());
		} catch (Exception e) {
			throw new OperException(无法删除有建筑物的校区);
		}
	}
	
	/**
	 * 删除校区。
	 * 当校区中存在下级建筑物信息时，不能删除。（这里会触发SQLException外键异常）
	 * 
	 * @param xq
	 * @throws OperException 无法删除有资产的部门
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delXQ(Integer xqid) throws OperException {
		try {
			xqDao.deleteByPrimaryKey(xqid);
		} catch (Exception e) {
			throw new OperException(无法删除有建筑物的校区);
		}
	}
	
	/**
	 * 统计校区列表
	 * @return
	 */
	public int countXQList(Integer appId) {
		CXQ param = new CXQ();
		param.setAppId(appId);
		
		return xqDao.countXQList(param);
	}
	
	/**
	 * 获取校区列表
	 * @return
	 */
	public List<CXQ> getXQList(Integer appId) {
		CXQ param = new CXQ();
		param.setAppId(appId);
		
		return xqDao.selectList(param);
	}
	
	/**
	 * 获取指定应用下的指定页的校区列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public List<CXQ> getXQList(Integer appId, int rows, int page) {
		CXQ param = new CXQ();
		param.setAppId(appId);
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return xqDao.selectList(param);
	}
	
	/**
	 * 获取指定应用下的指定页的校区列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public EasyuiDatagrid<CXQ> getPageXQList(Integer appId, int rows, int page) {
		int total = countXQList(appId);
		EasyuiDatagrid<CXQ> pageDatas = new EasyuiDatagrid<CXQ>();
		if(total == 0){
			pageDatas.setRows(new ArrayList<CXQ>());
			pageDatas.setTotal(total);
		}else{
			List<CXQ> list = getXQList(appId, rows, page);
			pageDatas.setTotal(total);
			pageDatas.setRows(list);
		}
		return pageDatas;			
	}
	
	/**
	 * 获得校区下拉框信息
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getXQCombo(Integer appId) {
		List<CXQ> list = getXQList(appId);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CXQ xq : list) {
				item = new EasyuiComboBoxItem();
				item.setId(xq.getId().toString());
				item.setText(xq.getXqmc());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 获得校区下拉框信息(包含全部)
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getXQComboWithAll(Integer appId) {
		List<CXQ> list = getXQList(appId);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CXQ xq : list) {
				item = new EasyuiComboBoxItem();
				item.setId(xq.getId().toString());
				item.setText(xq.getXqmc());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 获得校区下拉框信息, id和text均为校区名称。
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getXQMCCombo(Integer appId) {
		List<CXQ> list = getXQList(appId);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CXQ xq : list) {
				item = new EasyuiComboBoxItem();
				item.setId(xq.getXqmc());
				item.setText(xq.getXqmc());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 获得校区下拉框信息(包含全部), id和text均为校区名称。
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getXQMCComboWithAll(Integer appId) {
		List<CXQ> list = getXQList(appId);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CXQ xq : list) {
				item = new EasyuiComboBoxItem();
				item.setId(xq.getXqmc());
				item.setText(xq.getXqmc());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 根据主键获取校区数据
	 * @param id
	 * @exception 校区记录不存在
	 * @return
	 */
	public CXQ getXQ(Integer id) {
		CXQ xq = xqDao.selectByPrimaryKey(id);
		return xq;
	}
	/**
	 * 根据参数获取校区数据
	 * @param id
	 * @exception 校区记录不存在
	 * @return
	 */
	public CXQ getXQ(CXQ param) throws OperException {
		CXQ xq = xqDao.select(param);
		if(xq == null){
			throw new OperException(校区记录不存在);
		}
		return xq;
	}
}
