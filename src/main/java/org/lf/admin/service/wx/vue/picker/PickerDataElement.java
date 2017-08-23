package org.lf.admin.service.wx.vue.picker;

public class PickerDataElement {
	private String name;
	private String value;
	private String parent;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "PickerDataElement [name=" + name + ", value=" + value + ", parent=" + parent + "]";
	}

}
