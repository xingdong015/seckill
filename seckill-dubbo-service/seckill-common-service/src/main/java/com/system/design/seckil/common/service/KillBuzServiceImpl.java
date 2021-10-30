package com.system.design.seckil.common.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.system.design.seckill.common.api.IKillBuzService;
import com.system.design.seckill.common.api.IOrderService;
import com.system.design.seckill.common.api.IStockService;
import com.system.design.seckill.common.bean.Exposer;
import com.system.design.seckill.common.bean.RocketMqMessageBean;
import com.system.design.seckill.common.bean.SeckillResultStatus;
import com.system.design.seckill.common.entity.OrderEntity;
import com.system.design.seckill.common.exception.SeckillException;
import com.system.design.seckill.common.utils.CacheKey;
import com.system.design.seckill.common.utils.KillEventTopiEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
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
public class KillBuzServiceImpl implements IKillBuzService {
    @Autowired
    private RedisTemplate redisTemplate;
    @DubboReference
    private IOrderService orderService;
    @DubboReference
    private IStockService stockService;

//    @Autowired
//    private DefaultMQProducer defaultMQProducer;

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
    public Exposer exportKillUrl(long killId, long userId) {
        final Map<String, Object> killInfoMap = getById(String.valueOf(killId));
        //若是秒杀未开启
        long startTime = (long) killInfoMap.get(CacheKey.StockInfo.START_TIME);
        long endTime   = (long) killInfoMap.get(CacheKey.StockInfo.END_TIME);
        //系统当前时间
        long now = System.currentTimeMillis();
        if (startTime > now || endTime < now) {
            return Exposer.buildNotStaredExposer(killId, startTime, now - startTime);
        }
        //秒杀开启，返回秒杀商品的id、用给接口加密的md5
        String md5 = getMD5(killId, userId);
        return Exposer.buildHasStaredExpos(killId, md5, startTime, 0);
    }

    private String getMD5(long killId, long userId) {
        String base = killId + "/" + salt + "/" + userId;
        String md5  = DigestUtils.md5DigestAsHex(base.getBytes());
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
        //url重写、防止url破解
        if (md5 == null || !md5.equals(getMD5(killId, userId))) {
            return SeckillResultStatus.buildIllegalExecute(killId);
        }
        //重复秒杀、一个用户只允许秒杀一次
        if (redisTemplate.opsForSet().isMember(CacheKey.getSeckillBuyPhones(String.valueOf(killId)), userId)) {
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
            message.setTopic(KillEventTopiEnum.KILL_SUCCESS.getTopic());
            RocketMqMessageBean bean = new RocketMqMessageBean((userId + "-" + killId), null, System.currentTimeMillis());
            message.setBody(JSONObject.toJSONString(bean).getBytes(StandardCharsets.UTF_8));
            //这里有可能会投递失败、导致下单失败、所以实际情况下、redis的库存比数据库的库存多、
            //MySQL在真正扣减库存的时候需要通过乐观锁防止超卖
//            defaultMQProducer.sendOneway(message);
            return SeckillResultStatus.buildSuccessExecute(killId, result);
        } catch (Throwable e) {
            log.error("KillBuzServiceImpl#executeKill error:{} {}", killId, userId, e);
            return SeckillResultStatus.buildErrorExecute(killId);
        }
    }

    @Override
    public Long doKill(long killId, String userId) {

        int count = stockService.decreaseStorage(killId);
        Preconditions.checkArgument(count >= 1, "%s|%s|库存不足", killId, userId);

        OrderEntity order = orderService.createOrder(killId, userId);
        if (Objects.isNull(order)) {
            throw new SeckillException(String.format("order error => killId:%s userId:%s", killId, userId));
        }
        Preconditions.checkNotNull(order.getOrderId(), "%s|%s|订单创建失败", killId, userId);

//        addPayMonitor(order.getOrderId());

        return order.getOrderId();
    }

}
