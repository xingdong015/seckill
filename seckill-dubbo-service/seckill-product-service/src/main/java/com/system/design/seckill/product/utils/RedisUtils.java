package com.system.design.seckill.product.utils;

import com.system.design.seckill.product.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @description: 确保分布式锁可用，至少要确保锁的实现同时满足以下四个条件：
 * 1.互斥性。在任意时刻，只有一个客户端能持有锁。
 * 2.不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
 * 3.具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁和解锁。
 *   如果Redis是多机部署的，那么可以尝试使用Redisson实现分布式锁
 * 4.加解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 * @author: 贾凯
 * @create: 2021-11-12 12:36
 */
@Slf4j
@Component
public class RedisUtils {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    @Resource
    RedisConfig redisConfig;

    /**
     * @Description: 获取jedis客户端
     * @param:
     * @return Jedis
     * @author jiakai
     * @date 2021/11/12 13:19
    */
    public Jedis getJedis(){
        return redisConfig.jedisPoolFactory().getResource();
    }

    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }


    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

}
