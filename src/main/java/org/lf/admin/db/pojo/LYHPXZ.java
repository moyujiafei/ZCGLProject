package org.lf.admin.db.pojo;

public class LYHPXZ {
    private Integer id;

    private Integer jlId;

    private String mediaType;

    private String wxMediaId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJlId() {
        return jlId;
    }

    public void setJlId(Integer jlId) {
        this.jlId = jlId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType == null ? null : mediaType.trim();
    }

    public String getWxMediaId() {
        return wxMediaId;
    }

    public void setWxMediaId(String wxMediaId) {
        this.wxMediaId = wxMediaId == null ? null : wxMediaId.trim();
    }
}