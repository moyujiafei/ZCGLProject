package org.lf.admin.service.zcgl;

import java.util.ArrayList;
import java.util.List;

import org.lf.utils.EasyuiComboBoxItem;
import org.springframework.stereotype.Service;

/**
 * 任务类型
 */
@Service("rwlxService")
public class RWLXService {
	/**
	 * 任务类型下拉列表框（包括全部）
	 * @return
	 */
	public List<EasyuiComboBoxItem> getRWLXComboWithAll() {
		RWLX[] list = RWLX.values();
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.length > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (RWLX rwlx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(rwlx.getValue() + "");
				item.setText(rwlx.name());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 资产状态下拉列表框
	 * @return
	 */
	public List<EasyuiComboBoxItem> getRWLXCombo() {
		RWLX[] list = RWLX.values();
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.length > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item;
			for (RWLX rwlx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(rwlx.getValue() + "");
				item.setText(rwlx.name());
				combo.add(item);
			}
		}
		return combo;
	}
	
	public RWLX getRWLX(int value) {
		return RWLX.valueOf(value);
	}
}
