package com.system.design.seckill.common.api;

import com.system.design.seckill.common.bean.Exposer;
import com.system.design.seckill.common.bean.SeckillResultStatus;
import io.seata.spring.annotation.GlobalTransactional;

import java.util.List;
import java.util.Map;

/**
 * @author chengzhengzheng
 * @date 2021/10/28
 */
public interface IKillBuzService {

    List<Map<String, Object>> getSeckillList();

    Map<String, Object> getById(String killId);

    Exposer exportKillUrl(long killId, long userId);

    SeckillResultStatus executeKill(long killId, long userId, String md5);

    @GlobalTransactional(rollbackFor = Exception.class)
    Long doKill(long killId, String userId);
}