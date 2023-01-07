package com.xingdong.seckill.common.exception;

import com.xingdong.seckill.common.enums.SeckillStatusEnum;

/**
 * @author chengzhengzheng
 * @date 2021/11/6
 */
public class NoStockException extends SeckillException {
    public NoStockException(String message) {
        super(message, SeckillStatusEnum.NO_STOCK);
    }
}
