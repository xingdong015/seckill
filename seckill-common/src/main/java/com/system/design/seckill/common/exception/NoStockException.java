package com.system.design.seckill.common.exception;

import com.system.design.seckill.common.enums.SeckillStatusEnum;

/**
 * @author chengzhengzheng
 * @date 2021/11/6
 */
public class NoStockException extends SeckillException {
    public NoStockException(String message) {
        super(message, SeckillStatusEnum.NO_STOCK);
    }
}
