package org.lf.wx.menu;

import java.util.ArrayList;
import java.util.List;

import org.lf.utils.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SubMenu extends MenuItem {
	public static final String NAME = "name";
	public static final String SUB_BUTTON = "sub_button";
	
	private String name;
	private List<MenuItem> menuItemList = new ArrayList<>();
	
	public SubMenu(String name) {
		this(name, null);
	}
	
	public SubMenu(String name, List<MenuItem> menuItemList) {
		if (StringUtils.isEmpty(name)) {
			throw new NullPointerException();
		}
		this.name = name;
		
		if (menuItemList != null) {
			this.menuItemList.addAll(menuItemList);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	public List<MenuItem> getMenuItemList() {
		return menuItemList;
	}

	public void addMenuItem(MenuItem menu) {
		this.menuItemList.add(menu);
	}
	
	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		
		json.put(NAME, name);
		
		//add sub_button
		JSONArray array = new JSONArray();
		for (MenuItem item : menuItemList) {
			array.add(item.getJSON());
		}
		json.put(SUB_BUTTON, array);
		
		return json;
	}
	
	@Override
	public String toString() {
		return getJSON().toJSONString();
	}
}
