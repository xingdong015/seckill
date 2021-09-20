package com.system.design.seckill.bean;

import java.util.Date;

public class SuccessKilled {
    private long  seckillId;
    private long  userPhone;
    private short state;
    private Date  createTime;

    //多对一,因为一件商品在库存中有很多数量，对应的购买明细也有很多。
    private SeckillPo seckillPo;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SeckillPo getSeckill() {
        return seckillPo;
    }

    public void setSeckill(SeckillPo seckillPo) {
        this.seckillPo = seckillPo;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}