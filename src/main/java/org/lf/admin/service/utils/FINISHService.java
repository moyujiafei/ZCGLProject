package org.lf.admin.service.utils;

import java.util.ArrayList;
import java.util.List;

import org.lf.utils.EasyuiComboBoxItem;
import org.springframework.stereotype.Service;

/**
 * 是否完成
 */
@Service("finishService")
public class FINISHService {

	/**
	 * 完成下拉列表框
	 * @return
	 */
	public List<EasyuiComboBoxItem> getFinishCombo() {
		List<EasyuiComboBoxItem> items = new ArrayList<EasyuiComboBoxItem>();
		EasyuiComboBoxItem eItem = new EasyuiComboBoxItem();   //启用
		EasyuiComboBoxItem dItem = new EasyuiComboBoxItem();   //停用
		
		eItem.setId("0");
		eItem.setText("未完成");
		items.add(eItem);
		
		dItem.setId("1");
		dItem.setText("已完成");
		items.add(dItem);
		
		return items;
	}
	
	/**
	 * 完成下拉列表框（包含全部）
	 * @return
	 */
	public List<EasyuiComboBoxItem> getFinishComboWithAll() {
		List<EasyuiComboBoxItem> items = new ArrayList<EasyuiComboBoxItem>();
		EasyuiComboBoxItem aItem = new EasyuiComboBoxItem();   
		EasyuiComboBoxItem eItem = new EasyuiComboBoxItem();   
		EasyuiComboBoxItem dItem = new EasyuiComboBoxItem();  
		
		aItem.setId("");
		aItem.setText("全部");
		items.add(aItem);
		
		eItem.setId("0");
		eItem.setText("未完成");
		items.add(eItem);
		
		dItem.setId("1");
		dItem.setText("已完成");
		items.add(dItem);
		
		return items;
	}

}
