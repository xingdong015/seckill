package com.system.design.seckill.product.common.exception;

import com.system.design.seckill.product.common.enums.SeckillStatusEnum;

/**
 * 重复秒杀异常，是一个运行期异常，不需要我们手动try catch
 * Mysql只支持运行期异常的回滚操作
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message, SeckillStatusEnum.REPEAT_KILL);
    }
}