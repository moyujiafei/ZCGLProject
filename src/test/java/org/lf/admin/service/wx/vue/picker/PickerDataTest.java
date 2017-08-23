package org.lf.admin.service.wx.vue.picker;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PickerDataTest {
	
	/**
	 * 
	 * 一列选择示例
	 * 前台格式为
	 * name和value
	 * [[{
	 *   name: '2019届5班',
	 *   value: '1'
	 * }, {   
	 *   name: '2019届4班',  
	 *   value: '2'  
	 * }]]  
	 * 具体看官网api
	 * 
	 */
	@Test
	public void along(){
		
		//初始化
		PickerData pickerData = new PickerData();
		List<PickerDataElement> list = new ArrayList<>();
		
		//新建数据
		PickerDataElement a1 = new PickerDataElement();
		PickerDataElement a2 = new PickerDataElement();
		a1.setValue("1");
		a1.setName("2019届5班");
		a2.setValue("2");
		a2.setName("2019届4班");
		
		//放入数据
		list.add(a1);
		list.add(a2);
		
		//取出数据
		pickerData.setElementList(list);
		pickerData.getUnlinkNameAndValue();
		System.out.println(pickerData.getUnlinkNameAndValue());
	}

}
