package com.system.design.seckill.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.system.design.seckill.bean.Exposer;
import com.system.design.seckill.bean.RocketMqMessageBean;
import com.system.design.seckill.bean.SeckillResultStatus;
import com.system.design.seckill.entity.Seckill;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import com.system.design.seckill.utils.CacheKey;
import com.system.design.seckill.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@Service
@Slf4j
public class KillBuzServiceImpl extends ServiceImpl<SeckillInfoMapper, Seckill> implements KillBuzService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    //加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt = "cjy20200922czz0708";

    @Override
    public List<Map<String, Object>> getSeckillList() {
        //获取所有的秒杀id
        final Set<String> members = redisTemplate.opsForZSet().reverseRange(CacheKey.allSeckillIdZset(), 0, -1);
        final List<Object> killInfos = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (String member : members) {
                String key = CacheKey.getSeckillHash(member);
                connection.hGetAll(key.getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });
        return killInfos.stream().map(map -> (Map<String, Object>) map).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getById(String killId) {
        Map<byte[], byte[]> map     = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().hGetAll(CacheKey.getSeckillHash(killId).getBytes(StandardCharsets.UTF_8));
        Map<String, Object> results = new HashMap<>();
        map.forEach((bytes, bytes2) -> results.put(new String(bytes), new String(bytes2)));
        return results;
    }


    @Override
    public Exposer exportKillUrl(long killId) {
        final Map<String, Object> killInfoMap = getById(String.valueOf(killId));
        //若是秒杀未开启
        long startTime = (long) killInfoMap.get(CacheKey.StockInfo.START_TIME);
        long endTime   = (long) killInfoMap.get(CacheKey.StockInfo.END_TIME);
        //系统当前时间
        long now     = System.currentTimeMillis();
        if (startTime > now || endTime < now) {
            return Exposer.buildNotStaredExposer(killId, startTime, endTime);
        }
        //秒杀开启，返回秒杀商品的id、用给接口加密的md5
        String md5 = getMD5(killId);
        return Exposer.buildHasStaredExpos(killId, md5, startTime, endTime);
    }

    private String getMD5(long killId) {
        String base = killId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 开始秒杀活动
     *
     * @param killId
     * @param userId
     * @return
     */
    @Override
    public SeckillResultStatus executeKill(long killId, long userId, String md5) {
        if (md5 == null || !md5.equals(getMD5(killId))) {
            return SeckillResultStatus.buildIllegalExecute(killId);
        }
        if (redisTemplate.opsForSet().isMember(CacheKey.getSeckillBuyPhones(String.valueOf(killId)), userId)) {
            //重复秒杀、一个用户只允许秒杀一次
            return SeckillResultStatus.buildRepeatKillExecute(killId);
        }
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("stock.lua")));
        redisScript.setResultType(Long.class);
        try {
            Long result = (Long) redisTemplate.execute(redisScript, Lists.newArrayList(CacheKey.getSeckillHash(String.valueOf(killId)), "count"));
            if (result != null && result < 0) {
                return SeckillResultStatus.buildFailureExecute(killId);
            }
            redisTemplate.opsForSet().add(CacheKey.getSeckillBuyPhones(String.valueOf(killId)), userId);
            Message message = new Message();
            message.setTopic("topic_order");
            RocketMqMessageBean bean = new RocketMqMessageBean((userId + "-"+killId),System.currentTimeMillis());
            message.setBody(JsonUtils.objectToJson(bean).getBytes(StandardCharsets.UTF_8));
            defaultMQProducer.send(message);
            return SeckillResultStatus.buildSuccessExecute(killId, result);
        } catch (Throwable e) {
            log.error("KillBuzServiceImpl#executeKill error:{} {}", killId, userId, e);
            return SeckillResultStatus.buildErrorExecute(killId);
        }
    }

}
