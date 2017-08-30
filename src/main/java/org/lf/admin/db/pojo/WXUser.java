package org.lf.admin.db.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于在前端显示的微信用户信息 
 *
 */
public class WXUser {
    private Integer id;

    private String userid;

    private String name;

    private List<ChuWXDept> deptList = new ArrayList<>();

    private String position;

    private String mobile;

    private String sex; 

    private String email;
 
    private String weixinid;

    private String avatar;

    private String status;

    private String extattr;

    private Integer appId;

    private List<ChuTag> tagList = new ArrayList<ChuTag>();// 标签信息集合

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public List<ChuWXDept> getDeptList() {
		return deptList;
	}

	public void addDept(ChuWXDept dept) {
		this.deptList.add(dept);
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getWeixinid() {
        return weixinid;
    }

    public void setWeixinid(String weixinid) {
        this.weixinid = weixinid == null ? null : weixinid.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExtattr() {
        return extattr;
    }

    public void setExtattr(String extattr) {
        this.extattr = extattr == null ? null : extattr.trim();
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

	public List<ChuTag> getTagList() {
		return tagList;
	}

	public void addTag(ChuTag tag) {
		this.tagList.add(tag);
	}
    
}