package org.lf.admin.service.yhpgl;

import java.util.ArrayList;
import java.util.List;

import org.lf.utils.EasyuiComboBoxItem;
import org.springframework.stereotype.Service;

/**
 * 资产状态
 */
@Service("yhpCZLXService")
public class YHPCZLXService {
	/**
	 * 易耗品操作下拉列表框（包括全部）
	 * @return
	 */
	public List<EasyuiComboBoxItem> getYHPCZLXComboWithAll() {
		YHPCZLX[] list = YHPCZLX.values();
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.length > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (YHPCZLX czlx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(czlx.getValue() + "");
				item.setText(czlx.name());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 资产状态下拉列表框
	 * @return
	 */
	public List<EasyuiComboBoxItem> getYHPCZLXCombo() {
		YHPCZLX[] list = YHPCZLX.values();
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.length > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (YHPCZLX czlx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(czlx.getValue() + "");
				item.setText(czlx.name());
				combo.add(item);
			}
		}
		return combo;
	}
}
