package com.system.design.seckill.productwarmservcie.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @description: 缓存预热
 * @author: jack
 * @create: 2021-09-24 13:54
 */
@Component
@Slf4j
public class CacheWarmInit implements ApplicationRunner {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
