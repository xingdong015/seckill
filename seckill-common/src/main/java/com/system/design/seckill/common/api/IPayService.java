package com.system.design.seckill.common.api;

import com.system.design.seckill.common.bean.PayResultStatus;

/**
 * @author chengzhengzheng
 * @date 2021/10/25
 */
public interface IPayService {
    PayResultStatus pay(long orderId, long userId);
}