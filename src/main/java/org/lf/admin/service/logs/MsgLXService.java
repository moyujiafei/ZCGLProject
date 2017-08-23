package org.lf.admin.service.logs;

import java.util.ArrayList;
import java.util.List;

import org.lf.utils.EasyuiComboBoxItem;
import org.springframework.stereotype.Service;

@Service("msgLXService")
public class MsgLXService {
	/**
	 * 消息类型下拉列表框（包括全部）
	 * @return
	 */
	public List<EasyuiComboBoxItem> getMsgLXComboWithAll() {
		MsgLX[] list = MsgLX.values();
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.length > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = new EasyuiComboBoxItem();
			item.setId("");
			item.setText("全部");
			combo.add(item);
			for (MsgLX lx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(lx.getValue() + "");
				item.setText(lx.name());
				combo.add(item);
			}
		}
		return combo;
	}
	
	/**
	 * 消息类型下拉列表框
	 * @return
	 */
	public List<EasyuiComboBoxItem> getMsgLXCombo() {
		MsgLX[] list = MsgLX.values();
		List<EasyuiComboBoxItem> combo = null;
		if (list != null && list.length > 0) {
			combo = new ArrayList<EasyuiComboBoxItem>();
			EasyuiComboBoxItem item = null;
			for (MsgLX lx : list) {
				item = new EasyuiComboBoxItem();
				item.setId(lx.getValue() + "");
				item.setText(lx.name());
				combo.add(item);
			}
		}
		return combo;
	}

}
