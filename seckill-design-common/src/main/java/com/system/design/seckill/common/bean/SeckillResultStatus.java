package com.system.design.seckill.common.bean;

import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("all")
@Data
public class SeckillResultStatus implements Serializable {
    private long              seckillId;
    private SeckillStatusEnum statEnum;
    private long              stock;

    public SeckillResultStatus(long seckillId, Long result, SeckillStatusEnum success) {
        this.seckillId = seckillId;
        this.statEnum  = success;
        this.stock = result;
    }


    public static SeckillResultStatus buildSuccessExecute(long seckillId, Long result) {
        return new SeckillResultStatus(seckillId, result, SeckillStatusEnum.SUCCESS);
    }

    public static SeckillResultStatus buildFailureExecute(long seckillId) {
        return new SeckillResultStatus(seckillId, -1L, SeckillStatusEnum.END);
    }

    public static SeckillResultStatus buildErrorExecute(long seckillId) {
        return new SeckillResultStatus(seckillId, -1L, SeckillStatusEnum.INNER_ERROR);
    }

    public static SeckillResultStatus buildRepeatKillExecute(long seckillId) {
        return new SeckillResultStatus(seckillId, -1L, SeckillStatusEnum.REPEAT_KILL);
    }

    public static SeckillResultStatus buildIllegalExecute(long seckillId) {
        return new SeckillResultStatus(seckillId, -1L, SeckillStatusEnum.DATE_REWRITE);
    }
}