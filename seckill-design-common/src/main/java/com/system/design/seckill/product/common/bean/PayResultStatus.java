package com.system.design.seckill.product.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Data
@AllArgsConstructor
public class PayResultStatus {
    private long                orderId;
    private long                userId;
    private PayResultStatusEnum statEnum;

    public static PayResultStatus buildOrderExistedException(long orderId, long userId) {
        return new PayResultStatus(orderId,userId,PayResultStatusEnum.REPEAT_KILL);
    }

    public static PayResultStatus buildSuccessPay(long orderId, long userId) {
        return new PayResultStatus(orderId,userId,PayResultStatusEnum.SUCCESS);
    }

    public static PayResultStatus buildPayFail(long orderId, long userId) {
        return new PayResultStatus(orderId,userId,PayResultStatusEnum.FAIL);
    }

    public static PayResultStatus buildException(long orderId, long userId) {
        return new PayResultStatus(orderId,userId,PayResultStatusEnum.INNER_ERROR);
    }
}
