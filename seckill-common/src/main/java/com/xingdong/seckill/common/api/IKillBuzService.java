package com.xingdong.seckill.common.api;

import com.xingdong.seckill.common.bean.Exposer;
import com.xingdong.seckill.common.dto.SeckillDto;

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

    void executeKill(String killId, String userId, String md5);

    void addKill(SeckillDto seckillDto);
}
