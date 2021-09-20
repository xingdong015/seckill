package com.system.design.seckill.service;

import com.system.design.seckill.entity.Seckill;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import com.system.design.seckill.utils.RedisKeysWrapper;
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
public class CacheWarmService {
    @Autowired
    private SeckillInfoMapper seckillInfoMapper;
    @Autowired
    private RedisTemplate     redisTemplate;

    @PostConstruct
    public void init() {
        doInit();
    }

    private void doInit() {
        Set members = redisTemplate.opsForZSet().range(RedisKeysWrapper.allSeckillIdZset(), 0, -1);
        if (CollectionUtils.isEmpty(members)) {
            final List<Seckill> infoList = seckillInfoMapper.selctAll();
            if (CollectionUtils.isNotEmpty(infoList)) {
                Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
                for (Seckill seckill : infoList) {
                    tuples.add(ZSetOperations.TypedTuple.of(String.valueOf(seckill.getSeckillId()), (double) seckill.getStartTime()));
                    final Map<String, Object> values = new HashMap<>();
//                    values.put(RedisKeysWrapper.STOCK_COUNT, String.valueOf(seckill.getCount()));
//                    values.put(RedisKeysWrapper.STOCK_SALE, String.valueOf(seckill.getLockCount()));
//                    values.put(RedisKeysConstant.STOCK_VERSION, String.valueOf(seckill.getVersion()));
//                    values.put(RedisKeysWrapper.STOCK_ID, String.valueOf(seckill.getSeckillId()));
//                    values.put(RedisKeysWrapper.STOCK_NAME, String.valueOf(seckill.getSeckillName()));
                    redisTemplate.opsForHash().putAll(RedisKeysWrapper.getSeckillHash(String.valueOf(seckill.getSeckillId())), values);
                }
                redisTemplate.opsForZSet().add(RedisKeysWrapper.allSeckillIdZset(), tuples);
            }
        }
    }
}
