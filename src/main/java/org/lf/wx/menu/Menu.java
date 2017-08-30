package org.lf.wx.menu;

import java.util.ArrayList;
import java.util.List;

import org.lf.wx.utils.WXJSON;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Menu extends WXJSON {
	public static final String BUTTON = "button";
	
	private List<MenuItem> menuItemList = new ArrayList<>();
	
	public Menu() {
		
	}
	
	public Menu append(MenuItem item) {
		menuItemList.add(item);
		return this;
	}
	
	public Menu append(SubMenu subMenu) {
		menuItemList.add(subMenu);
		return this;
	}
	
	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		
		JSONArray array = new JSONArray();
		for (MenuItem item : menuItemList) {
			array.add(item.getJSON());
		}
		
		json.put(BUTTON, array);
		
		return json;
	}
}
