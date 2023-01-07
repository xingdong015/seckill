package com.xingdong.seckill.common.api;

import com.xingdong.seckill.common.bean.PayResultStatus;

/**
 * @author chengzhengzheng
 * @date 2021/10/25
 */
public interface IPayService {
    PayResultStatus pay(long orderId, long userId);
}
