package com.system.design.seckill.bean;

import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("all")
@Data
public class SeckillResultStatus implements Serializable {
    private long            seckillId;
    private SeckillStatEnum statEnum;
    private long            stock;

    public SeckillResultStatus(long seckillId, Long result, SeckillStatEnum success) {
        this.seckillId = seckillId;
        this.statEnum  = success;
        this.stock = result;
    }


    public static SeckillResultStatus buildSuccessExecute(long seckillId, Long result) {
        return new SeckillResultStatus(seckillId, result,SeckillStatEnum.SUCCESS);
    }

    public static SeckillResultStatus buildFailureExecute(long seckillId) {
        return new SeckillResultStatus(seckillId, -1L, SeckillStatEnum.END);
    }

    public static SeckillResultStatus buildErrorExecute(long seckillId) {
        return new SeckillResultStatus(seckillId, -1L, SeckillStatEnum.INNER_ERROR);
    }
}