package com.xingdong.seckill.common.exception;

import com.xingdong.seckill.common.enums.SeckillStatusEnum;

/**
 * 秒杀关闭异常，当秒杀结束时用户还要进行秒杀就会出现这个异常
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message, SeckillStatusEnum.END);
    }
}
