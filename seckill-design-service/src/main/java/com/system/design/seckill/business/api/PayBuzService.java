package com.system.design.seckill.business.api;

import com.system.design.seckill.bean.PayResultStatus;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
public interface PayBuzService {
    PayResultStatus pay(long orderId, long userId);
}
