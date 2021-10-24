package com.system.design.seckill.service.api;

import io.seata.spring.annotation.GlobalTransactional;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
public interface OrderBuzService {
    @GlobalTransactional(rollbackFor = Exception.class)
    Long doKill(long killId, String userId);
}
