package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.List;

import org.lf.utils.EasyuiComboBoxItem;
import org.springframework.stereotype.Service;

/**
 * 资产状态
 */
@Service("zcztService")
public class ZCZTService {
	/**
	 * 资产状态下拉列表框（包括全部）
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCZTComboWithAll() {
		ZCZT[] list = ZCZT.values();
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.length > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (ZCZT zczt : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zczt.getValue() + "");
				item.setText(zczt.name());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 资产状态下拉列表框
	 * @return
	 */
	public List<EasyuiComboBoxItem> getZCZTCombo() {
		ZCZT[] list = ZCZT.values();
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.length > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (ZCZT zczt : list) {
				item = new EasyuiComboBoxItem();
				item.setId(zczt.getValue() + "");
				item.setText(zczt.name());
				combo.add(item);
			}
		}
		return combo;
	}
}
