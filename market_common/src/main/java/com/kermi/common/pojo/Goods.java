package com.kermi.common.pojo;

public class Goods {
    private Long id;

    private String gname;

    private Double goprice;

    private Integer gstore;

    private String gpicture;

    private Long goodstypeId;

    private Long businessId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname == null ? null : gname.trim();
    }

    public Double getGoprice() {
        return goprice;
    }

    public void setGoprice(Double goprice) {
        this.goprice = goprice;
    }

    public Integer getGstore() {
        return gstore;
    }

    public void setGstore(Integer gstore) {
        this.gstore = gstore;
    }

    public String getGpicture() {
        return gpicture;
    }

    public void setGpicture(String gpicture) {
        this.gpicture = gpicture == null ? null : gpicture.trim();
    }

    public Long getGoodstypeId() {
        return goodstypeId;
    }

    public void setGoodstypeId(Long goodstypeId) {
        this.goodstypeId = goodstypeId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
}