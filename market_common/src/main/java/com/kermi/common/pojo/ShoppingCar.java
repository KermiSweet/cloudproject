package com.kermi.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCar {
    private Long id;

    private Long usertableId;

    private Long goodstableId;

    private Integer shoppingnum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsertableId() {
        return usertableId;
    }

    public void setUsertableId(Long usertableId) {
        this.usertableId = usertableId;
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