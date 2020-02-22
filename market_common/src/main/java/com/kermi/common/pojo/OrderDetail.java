package com.kermi.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private Long id;

    private Long orderbasetableId;

    private Long goodstableId;

    private Integer shoppingnum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderbasetableId() {
        return orderbasetableId;
    }

    public void setOrderbasetableId(Long orderbasetableId) {
        this.orderbasetableId = orderbasetableId;
    }

    public Long getGoodstableId() {
        return goodstableId;
    }

    public void setGoodstableId(Long goodstableId) {
        this.goodstableId = goodstableId;
    }

    public Integer getShoppingnum() {
        return shoppingnum;
    }

    public void setShoppingnum(Integer shoppingnum) {
        this.shoppingnum = shoppingnum;
    }
}