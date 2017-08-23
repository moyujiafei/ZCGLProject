package org.lf.admin.db.pojo;

public class CFJ extends PagedPojo {
    private Integer id;

    private Integer appId;

    private Integer jzwId;

    private String floor;

    private String room;

    private String deptName;

    private String glr;

    private Integer tybz;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getJzwId() {
        return jzwId;
    }

    public void setJzwId(Integer jzwId) {
        this.jzwId = jzwId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room == null ? null : room.trim();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getGlr() {
        return glr;
    }

    public void setGlr(String glr) {
        this.glr = glr == null ? null : glr.trim();
    }

    public Integer getTybz() {
        return tybz;
    }

    public void setTybz(Integer tybz) {
        this.tybz = tybz;
    }
}