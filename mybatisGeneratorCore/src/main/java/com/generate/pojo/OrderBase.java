package com.generate.pojo;

import java.util.Date;

public class OrderBase {
    private Long id;

    private Long usertableId;

    private Double amount;

    private Byte status;

    private Date orderdate;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }
}