package org.lf.admin.service.catalog;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.CJZLXMapper;
import org.lf.admin.db.pojo.CJZLX;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.PageNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 建筑类型
 * 
 * @author 石小平
 */
@Service("jzlxService")
public class JZLXService {

    @Autowired
	private CJZLXMapper jzlxDao;
	
	public static final OperErrCode 建筑类型记录不存在 = new OperErrCode("10201", "建筑类型记录不存在");
	/**
	 * 统计建筑类型列表
	 * @param param
	 * @return
	 */
	
	public int countJZLXList(CJZLX param) {
		return jzlxDao.countJZLXList(param);
	}
	
	/**
	 * 获取建筑类型列表
	 * @param param
	 * @return
	 */
	public List<CJZLX> getJZLXList(CJZLX param) {
         return jzlxDao.getJZLXList(param);
	}
	
	/**
	 * 获取指定应用下的指定页的建筑类型列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public List<CJZLX> getJZLXList(CJZLX param, int rows, int page) {
		PageNavigator pn = new PageNavigator(rows, page);
		param.setStart(pn.getStart());
		param.setOffset(pn.getOffset());
		return getJZLXList(param);
	}
	
	/**
	 * 获取指定应用下的指定页的建筑类型列表。
	 * @param rows 一页的记录数
	 * @param page 当前页号
	 * @return
	 */
	public EasyuiDatagrid<CJZLX> getPageJZLXList(CJZLX param,int rows, int page) {
		int total = countJZLXList(param);

		EasyuiDatagrid<CJZLX> pageDatas = new EasyuiDatagrid<CJZLX>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<CJZLX>());
		} else {
			List<CJZLX> list = getJZLXList(param, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;	
	}
	
	public EasyuiDatagrid<CJZLX> getPageJZLXList(int rows, int page) {
		CJZLX param = new CJZLX();
		return getPageJZLXList(param, rows, page);
	}
	
	/**
	 * 获得建筑类型下拉框信息
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getJZLXCombo() {
		List<CJZLX> list = getJZLXList(null);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CJZLX jzlx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(jzlx.getId().toString());
				item.setText(jzlx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}
	/**
	 * 获得建筑类型下拉框信息(包含全部)
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getJZLXComboWithAll() {
		List<CJZLX> list = getJZLXList(null);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CJZLX jzlx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(jzlx.getId().toString());
				item.setText(jzlx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}
	/**
	 * 获得建筑类型下拉框信息,id与text相同
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getJZLXMCCombo() {
		List<CJZLX> list = getJZLXList(null);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (CJZLX jzlx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(jzlx.getMc());
				item.setText(jzlx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}
	/**
	 * 获得建筑类型下拉框信息(包含全部)，id与text相同
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getJZLXMCComboWithAll() {
		List<CJZLX> list = getJZLXList(null);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (CJZLX jzlx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(jzlx.getMc());
				item.setText(jzlx.getMc());
				combo.add(item);
			}
		}
		return combo;
	}
	/**
	 * 根据主键获取建筑类型数据
	 * @param id
	 * @exception 建筑类型记录不存在
	 * @return
	 */
	public CJZLX getJZLX(Integer id) throws OperException {
		CJZLX param=new CJZLX();
		param.setId(id);
		CJZLX jzlx=jzlxDao.select(param);
		if(jzlx==null){
			throw new OperException(建筑类型记录不存在);
		}
		return jzlx;
	}
	
	/**
	 * 根据CJZLX对象获取建筑类型数据
	 * @param id
	 * @exception 建筑类型记录不存在
	 * @return
	 */
	public CJZLX getJZLX(CJZLX param) throws OperException {
		CJZLX jzlx = jzlxDao.select(param);
		if (jzlx == null) {
			throw new OperException(建筑类型记录不存在);
		}
		return jzlx;		
	}

}
