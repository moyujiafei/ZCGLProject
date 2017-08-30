package org.lf.admin.service.wx.vue.picker;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;	

public class PickerData {
	private List<PickerDataElement> elementList = new ArrayList<>();
	
	private Integer level;
	
	private Object res;
	public Object getRes() {
		return res;
	}

	public void setRes(Object res) {
		this.res = res;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<PickerDataElement> getElementList() {
		return elementList;
	}

	public void setElementList(List<PickerDataElement> elementList) {
		this.elementList = elementList;
	}
	
	
	/**
	 * 返回联动时数据格式
	 * 联动时，列表数据格式示例：
	 * [{
	 *   name: '中国',
	 *   value: 'china',
	 *   parent: 0
	 *   }, {
	 *   name: '广东',
	 *   value: 'china001',
	 *   parent: 'china'
	 *   }]
	 * @return
	 */
	public List<PickerDataElement> getLinkArray() {
		if (elementList != null && elementList.size() > 0) {
			return elementList;
		} else {
			return null;
		}
	}
	
	
	/**
	 * 返回一种非联动的数据格式
	 * 
	 * 非联动情况下，列表数据格式
	 * name和value
	 * [[{
	 *   name: '2019届5班',
	 *   value: '1'
	 * }, {   
	 *   name: '2019届4班',  
	 *   value: '2'  
	 * }]]  
	 * @return
	 */
	public JSONArray getUnlinkNameAndValue() {
		if (elementList != null && elementList.size() > 0) {
			JSONArray array = new JSONArray();
			array.add(elementList);
			return array;
		} else {
			return null;
		}
	}
	
	/**
	 * 返回一种非联动的数据格式
	 * 
	 * 只有value
	 * ['小米', '小米1']
	 * 
	 * @return
	 */
	public String[] getUnlinkOnlyName() {
		if (elementList != null && elementList.size() > 0) {
			String[] array = new String[elementList.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = elementList.get(i).getValue();
			}
			return array;
		} else {
			return null;
		}
	}
	
}
