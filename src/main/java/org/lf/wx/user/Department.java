package org.lf.wx.user;

import org.lf.wx.utils.WXJSON;

import com.alibaba.fastjson.JSONObject;

/**
 * 部门
 * http://qydev.weixin.qq.com/wiki/index.php?title=%E7%AE%A1%E7%90%86%E9%83%A8
 * %E9%97%A8
 */
public class Department extends WXJSON {
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String ORDER = "order";
	public static final String PARENTID = "parentid";

	private Integer id;
	private String name;
	private Integer order;
	private Integer parentid;

	public Department(JSONObject json) {
		this.id = json.getIntValue(ID);
		this.name = json.getString(NAME);
		this.order = json.getIntValue(ORDER);
		this.parentid = json.getIntValue(PARENTID);
	}

	public Department(Integer id, String name, Integer order, Integer parentid) {
		super();
		
		if (name.length() > 32) {
			throw new IllegalArgumentException("部门名称长度限制为32个字（汉字或英文字母）");
		}

		this.id = id;
		this.name = name;
		this.order = order;
		this.parentid = parentid == null ? 1 : parentid;
	}
	
	public Department(String name) {
		this(null, name, null, null);
	}
	
	public Department(String name, Integer parentid) {
		this(null, name, null, parentid);
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getOrder() {
		return order;
	}

	public Integer getParentid() {
		return parentid;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		json.put(Department.NAME, name);
		json.put(Department.PARENTID, parentid);
		if (order != null) {
			json.put(ORDER, order);
		}
		if (id != null) {
			json.put(ID, id);
		}

		return json;
	}

}
