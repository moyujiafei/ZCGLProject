package org.lf.wx.menu;

import org.lf.utils.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 自定义菜单能够帮助公众号丰富界面，让用户更好更快地理解公众号的功能。
 *
 */
public class MenuItem {
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String KEY = "key";
	public static final String URL = "url";
	
	private MenuType type;
	private String name;
	private String key;
	private String url;
	
	protected MenuItem() {}
	
	/**
	 * 创建非View类型的按钮菜单(包括View)
	 * 
	 * @param type 菜单的响应动作类型，注意：不能是view类型的按钮。
	 * @param name 菜单标题，不超过16个字节，子菜单不超过40个字节
	 * @param key 菜单KEY值，用于消息接口推送，不超过128字节
	 */
	public static MenuItem createMenuItem(MenuType type, String name, String key) {
		if (type == null || StringUtils.isEmpty(name) || StringUtils.isEmpty(key)) {
			throw new NullPointerException();
		}
		
		if (type == MenuType.view) {
			throw new IllegalArgumentException("Menu type should not be view");
		}
		
		MenuItem menu = new MenuItem();
		
		menu.type = type;
		menu.name = name;
		menu.key = key;
		
		return menu;
	}

	/**
	 * 创建View类型的按钮菜单
	 * 
	 * @param name 菜单标题，不超过16个字节，子菜单不超过40个字节
	 * @param url 网页链接，用户点击菜单可打开链接，不超过1024字节 
	 */
	public static MenuItem createViewMenuItem(String name, String url) {
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(url)) {
			throw new NullPointerException();
		}
		
		MenuItem menu = new MenuItem();
		
		menu.type = MenuType.view;
		menu.name = name;
		menu.url = url;
		
		return menu;
	}
	
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		
		json.put(NAME, name);
		json.put(TYPE, type);
		if (this.key != null) {
			json.put(KEY, key);
		}
		if (this.url != null) {
			json.put(URL, url);
		}
		
		return json;
	}

	@Override
	public String toString() {
		return getJSON().toJSONString();
	}

	public MenuType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}
	
	public String getUrl() {
		return url;
	}
}
