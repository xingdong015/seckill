package com.system.design.seckill.service;

import com.system.design.seckill.entity.SeckillInfo;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import com.system.design.seckill.utils.RedisKeysConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chengzhengzheng
 * @date 2021/9/20
 */
@Service
public class InitService {

    @Autowired
    private SeckillInfoMapper seckillInfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostConstruct
    public void init() {
//        doInit();
    }

    private void doInit() {
        Set members = redisTemplate.opsForZSet().range(RedisKeysConstant.allSeckillInfo(), 0, -1);
        if (CollectionUtils.isEmpty(members)) {
            final List<SeckillInfo> infoList = seckillInfoMapper.selctAll();
            if (CollectionUtils.isNotEmpty(infoList)) {
                Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
                for (SeckillInfo seckillInfo : infoList) {
                    tuples.add(ZSetOperations.TypedTuple.of(String.valueOf(seckillInfo.getSeckillId()), (double) seckillInfo.getCtime()));
                    final Map<String, Object> values = new HashMap<>();
                    values.put(RedisKeysConstant.STOCK_COUNT, String.valueOf(seckillInfo.getCount()));
                    values.put(RedisKeysConstant.STOCK_SALE, String.valueOf(seckillInfo.getLockCount()));
                    values.put(RedisKeysConstant.STOCK_VERSION, String.valueOf(seckillInfo.getVersion()));
                    values.put(RedisKeysConstant.STOCK_ID, String.valueOf(seckillInfo.getSeckillId()));
                    values.put(RedisKeysConstant.STOCK_NAME, String.valueOf(seckillInfo.getSeckillName()));
                    redisTemplate.opsForHash().putAll(RedisKeysConstant.getSeckillInfo(String.valueOf(seckillInfo.getSeckillId())), values);
                }
                redisTemplate.opsForZSet().add(RedisKeysConstant.allSeckillInfo(), tuples);
            }
        }

    }

}
