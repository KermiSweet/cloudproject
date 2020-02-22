package com.kermi.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Focus {
    private Long id;

    private Long goodstableId;

    private Long usertableId;

    private Date focustime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodstableId() {
        return goodstableId;
    }

    public void setGoodstableId(Long goodstableId) {
        this.goodstableId = goodstableId;
    }

    public Long getUsertableId() {
        return usertableId;
    }

    public void setUsertableId(Long usertableId) {
        this.usertableId = usertableId;
    }

    public Date getFocustime() {
        return focustime;
    }

    public void setFocustime(Date focustime) {
        this.focustime = focustime;
    }
}