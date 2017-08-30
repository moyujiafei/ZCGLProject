package org.lf.admin.db.pojo;

public class JYHPSQXZ {
    private Integer id;

    private String sqDm;

    private Integer yhpId;

    private Integer sqNum;

    private Integer spNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSqDm() {
        return sqDm;
    }

    public void setSqDm(String sqDm) {
        this.sqDm = sqDm == null ? null : sqDm.trim();
    }

    public Integer getYhpId() {
        return yhpId;
    }

    public void setYhpId(Integer yhpId) {
        this.yhpId = yhpId;
    }

    public Integer getSqNum() {
        return sqNum;
    }

    public void setSqNum(Integer sqNum) {
        this.sqNum = sqNum;
    }

    public Integer getSpNum() {
        return spNum;
    }

    public void setSpNum(Integer spNum) {
        this.spNum = spNum;
    }
}