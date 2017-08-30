package org.lf.admin.service.sys;

import java.util.ArrayList;
import java.util.List;

import org.lf.admin.db.dao.ChuTagMapper;
import org.lf.admin.db.pojo.ChuTag;
import org.lf.utils.EasyuiComboBoxItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wxtagService")
public class WXTagService {
	@Autowired
	private ChuTagMapper tagDao;
	
	/**
	 * 获得部门下拉框信息(包含全部)
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getWXTagComboWithAll(Integer appId) {
		ChuTag param = new ChuTag();
		param.setAppId(appId);
		List<ChuTag> list = tagDao.selectList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (ChuTag xq : list) {
				item = new EasyuiComboBoxItem();
				item.setId(xq.getTagNo().toString());
				item.setText(xq.getTagName());
				combo.add(item);
			}
		}
		return combo;
	}

	/**
	 * 获得部门下拉框信息
	 * 
	 * @return
	 */
	public List<EasyuiComboBoxItem> getWXTagCombo(Integer appId) {
		ChuTag param = new ChuTag();
		param.setAppId(appId);
		List<ChuTag> list = tagDao.selectList(param);
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.size() > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (ChuTag xq : list) {
				item = new EasyuiComboBoxItem();
				item.setId(xq.getTagNo().toString());
				item.setText(xq.getTagName());
				combo.add(item);
			}
		}
		return combo;
	}
}
