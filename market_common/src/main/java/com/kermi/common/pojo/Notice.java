package com.kermi.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    private Long id;

    private String ntitle;

    private String ncontent;

    private Date ntime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNtitle() {
        return ntitle;
    }

    public void setNtitle(String ntitle) {
        this.ntitle = ntitle == null ? null : ntitle.trim();
    }

    public String getNcontent() {
        return ncontent;
    }

    public void setNcontent(String ncontent) {
        this.ncontent = ncontent == null ? null : ncontent.trim();
    }

    public Date getNtime() {
        return ntime;
    }

    public void setNtime(Date ntime) {
        this.ntime = ntime;
    }
}