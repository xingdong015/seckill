package com.xingdong.seckill.common.exception;

import com.xingdong.seckill.common.enums.SeckillStatusEnum;

/**
 * 秒杀相关的所有业务异常
 */
public class SeckillException extends RuntimeException {
    private SeckillStatusEnum statusEnum;
    public SeckillException(String message, SeckillStatusEnum statusEnum) {
        super(message);
        this.statusEnum = statusEnum;
    }

    public SeckillStatusEnum getStatusEnum() {
        return statusEnum;
    }
}
