package com.system.design.seckill.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.system.design.seckill.entity.SeckillInfo;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import com.system.design.seckill.utils.RedisKeysConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
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
@Slf4j
public class InitService {

    @Autowired
    private SeckillInfoMapper seckillInfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostConstruct
    public void init() {
        doInit();
    }

    private void doInit(){
        //1）mysql获取所有秒杀产品信息；
        final List<SeckillInfo> seckillInfos = seckillInfoMapper.selctAll();
        if (CollectionUtils.isEmpty(seckillInfos)){
            log.info("无秒杀产品信息");
            return;
        }

        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                //2）删除redis旧缓存；
                Long removeCount = redisTemplate.opsForZSet().removeRange(RedisKeysConstant.allSeckillInfo(), 0, -1);
                log.info("缓存预热，移除秒杀id个数:{}",removeCount);
                seckillInfos.stream().forEach(seckillInfo -> {
                    Boolean delete = redisTemplate.delete(RedisKeysConstant.getSeckillInfo(String.valueOf(seckillInfo.getSeckillId())));
                    if (delete) {
                        log.info("缓存预热，删除秒杀key:{}", RedisKeysConstant.getSeckillInfo(String.valueOf(seckillInfo.getSeckillId())));
                    }
                });
                //3）写入秒杀信息（sort set存所有秒杀id； hash存秒杀产品信息）
                Set<ZSetOperations.TypedTuple<String>> tupleHashSet = new HashSet<>();
                seckillInfos.stream().forEach(seckillInfo -> {
                    tupleHashSet.add(ZSetOperations.TypedTuple.of(String.valueOf(seckillInfo.getSeckillId()), (double)seckillInfo.getCtime()));
                    HashMap<String, Object> values = new HashMap<>();
                    values.put(RedisKeysConstant.STOCK_COUNT,seckillInfo.getCount());
                    values.put(RedisKeysConstant.STOCK_ID,seckillInfo.getSeckillId());
                    values.put(RedisKeysConstant.STOCK_NAME,seckillInfo.getSeckillName());
                    values.put(RedisKeysConstant.STOCK_SALE,seckillInfo.getLockCount());
                    values.put(RedisKeysConstant.STOCK_VERSION,seckillInfo.getVersion());
                    redisTemplate.opsForHash().putAll(RedisKeysConstant.getSeckillInfo(String.valueOf(seckillInfo.getSeckillId())), values);
                    log.info("缓存预热，增加秒杀key:{}", RedisKeysConstant.getSeckillInfo(String.valueOf(seckillInfo.getSeckillId())));
                });
                Long addCount = redisTemplate.opsForZSet().add(RedisKeysConstant.allSeckillInfo(), tupleHashSet);
                log.info("缓存预热，增加秒杀key个数:{}",addCount);
                return null;
            }
        });
    }


//    private void doInit() {
//        Set members = redisTemplate.opsForZSet().range(RedisKeysConstant.allSeckillInfo(), 0, -1);
//        if (CollectionUtils.isEmpty(members)) {
//            final List<SeckillInfo> infoList = seckillInfoMapper.selctAll();
//            if (CollectionUtils.isNotEmpty(infoList)) {
//                Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
//                for (SeckillInfo seckillInfo : infoList) {
//                    tuples.add(ZSetOperations.TypedTuple.of(String.valueOf(seckillInfo.getSeckillId()), (double) seckillInfo.getCtime()));
//                    final Map<String, Object> values = new HashMap<>();
//                    values.put(RedisKeysConstant.STOCK_COUNT, String.valueOf(seckillInfo.getCount()));
//                    values.put(RedisKeysConstant.STOCK_SALE, String.valueOf(seckillInfo.getLockCount()));
//                    values.put(RedisKeysConstant.STOCK_VERSION, String.valueOf(seckillInfo.getVersion()));
//                    values.put(RedisKeysConstant.STOCK_ID, String.valueOf(seckillInfo.getSeckillId()));
//                    values.put(RedisKeysConstant.STOCK_NAME, String.valueOf(seckillInfo.getSeckillName()));
//                    redisTemplate.opsForHash().putAll(RedisKeysConstant.getSeckillInfo(String.valueOf(seckillInfo.getSeckillId())), values);
//                }
//                redisTemplate.opsForZSet().add(RedisKeysConstant.allSeckillInfo(), tuples);
//            }
//        }
//
//    }

}
