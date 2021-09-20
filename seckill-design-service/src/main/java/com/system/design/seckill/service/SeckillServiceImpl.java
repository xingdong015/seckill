package com.system.design.seckill.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.system.design.seckill.bean.Exposer;
import com.system.design.seckill.bean.SeckillResultStatus;
import com.system.design.seckill.entity.Seckill;
import com.system.design.seckill.exception.RepeatKillException;
import com.system.design.seckill.exception.SeckillCloseException;
import com.system.design.seckill.exception.SeckillException;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import com.system.design.seckill.utils.RedisKeysWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@Service
@Slf4j
public class SeckillServiceImpl extends ServiceImpl<SeckillInfoMapper, Seckill> implements SeckillBuzService {

    @Autowired
    private RedisTemplate     redisTemplate;
    @Autowired
    private ObjectMapper      mapper;
    @Autowired
    private SeckillInfoMapper seckillInfoMapper;


    @Override
    public List<Map<String, Object>> getSeckillList() {
        //获取所有的秒杀id
        final Set<String> members = redisTemplate.opsForZSet().reverseRange(RedisKeysWrapper.allSeckillIdZset(), 0, -1);
        final List<Object> killInfos = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (String member : members) {
                String key = RedisKeysWrapper.getSeckillHash(member);
                connection.hGetAll(key.getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });
        return killInfos.stream().map(map -> (Map<String, Object>) map).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getById(String killId) {
        Map<byte[], byte[]> map     = redisTemplate.getConnectionFactory().getConnection().hGetAll(RedisKeysWrapper.getSeckillHash(killId).getBytes(StandardCharsets.UTF_8));
        Map<String, Object> results = new HashMap<>();
        map.forEach((bytes, bytes2) -> {
            results.put(new String(bytes), new String(bytes2));
        });
        return results;
    }


    @Override
    public Exposer exportKillUrl(long killId) {

        return null;
    }

    /**
     * 开始秒杀活动
     *
     * @param killId
     * @param userPhone
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    @Override
    public SeckillResultStatus executeKill(long killId, long userPhone)  {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("stock.lua")));
        redisScript.setResultType(Long.class);
        try {
            //1. redis扣减库存
            Long result = (Long) redisTemplate.execute(redisScript, Lists.newArrayList(RedisKeysWrapper.getSeckillHash(String.valueOf(killId)), "count"));
            if (result < 0) {
                //3. 扣减失败、返回已经抢空给端上
                return SeckillResultStatus.buildFailureExecute(killId);
            }
            //2. 扣减成功、发送mq消息 TODO
            return SeckillResultStatus.buildSuccessExecute(killId, result);
        } catch (Throwable e) {
            log.error("SeckillServiceImpl#executeKill error:{} {}", killId, userPhone, e);
            return SeckillResultStatus.buildErrorExecute(killId);
        }
    }

}
