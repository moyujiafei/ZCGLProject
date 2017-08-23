package org.lf.admin.service.utils;

import java.util.ArrayList;
import java.util.List;

import org.lf.utils.EasyuiComboBoxItem;
import org.springframework.stereotype.Service;

/**
 * 停用标志
 */
@Service("tybzService")
public class TYBZService {
	/**
	 * 停用标志下拉列表框
	 * @return
	 */
	public List<EasyuiComboBoxItem> getTYBZCombo() {
		List<EasyuiComboBoxItem> items = new ArrayList<EasyuiComboBoxItem>();
		EasyuiComboBoxItem eItem = new EasyuiComboBoxItem();   //启用
		EasyuiComboBoxItem dItem = new EasyuiComboBoxItem();   //停用
		eItem.setId("0");
		eItem.setText("启用");
		items.add(eItem);
		
		dItem.setId("1");
		dItem.setText("停用");
		items.add(dItem);
		return items;
	}
}
