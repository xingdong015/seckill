package com.xingdong.seckill.business.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.xingdong.seckill.common.api.IAccountService;
import com.xingdong.seckill.common.api.IKillBuzService;
import com.xingdong.seckill.common.bean.Exposer;
import com.xingdong.seckill.common.bean.RocketMqMessageBean;
import com.xingdong.seckill.common.dto.SeckillDto;
import com.xingdong.seckill.common.exception.NoStockException;
import com.xingdong.seckill.common.exception.RepeatKillException;
import com.xingdong.seckill.common.exception.SeckillCloseException;
import com.xingdong.seckill.common.po.Account;
import com.xingdong.seckill.common.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
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
@DubboService(protocol = "dubbo", version = "1.0.0")
@Slf4j
public class KillBuzService implements IKillBuzService {
    @Resource
    private RedisTemplate    redisTemplate;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private IAccountService  accountService;
    @Value("${kill.url.salt}")
    private String           salt;
    @Value("${kill.rocketmq.producer.topic}")
    private String           topic;

    @Override
    public List<Map<String, Object>> getSeckillList() {
        //?????????????????????id  TODO ?????????????????? guava Cache??????caffeine??????????????????
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
        //?????????????????????
        long startTime = (long) killInfoMap.get(CacheKey.StockInfo.START_TIME);
        long endTime   = (long) killInfoMap.get(CacheKey.StockInfo.END_TIME);
        //??????????????????
        long now = System.currentTimeMillis();
        if (startTime > now || endTime < now) {
            return Exposer.buildNotStaredExposer(killId, startTime, now - startTime);
        }
        //????????????????????????????????????id????????????????????????md5
        String md5 = getMD5(killId, userId);
        return Exposer.buildHasStaredExpos(killId, md5, startTime, 0);
    }

    private String getMD5(long killId, long userId) {
        String base = killId + "/" + salt + "/" + userId;
        String md5  = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * ??????????????????
     *
     * @param killIdStr
     * @param userIdStr
     * @return
     */
    @Override
    public void executeKill(String killIdStr, String userIdStr, String md5) {
        Preconditions.checkArgument(NumberUtils.isDigits(killIdStr), "??????id");
        Preconditions.checkArgument(NumberUtils.isDigits(userIdStr), "??????id??????");
        long    killId  = Long.parseLong(killIdStr);
        long    userId  = Long.parseLong(userIdStr);
        Account account = accountService.findById(userId);
        if (account == null) {
            throw new RuntimeException("???????????????!");
        }
        //???????????????????????????
        urlCheck(killId, userId, md5);
        //??????????????????????????????????????????????????????
        repeatKillCheck(killId, userId);
        //redis??????????????? ?????? TODO ?????????script?????????redis???
        decreaseCacheStock(killId);
        //???????????????????????????
        redisTemplate.opsForSet().add(CacheKey.getSeckillBuyPhones(String.valueOf(killId)), userId);
        //?????????????????????????????????????????????
        sendKillSuccessMessage(killId, userId);
    }

    private void sendKillSuccessMessage(long killId, long userId) {
        RocketMqMessageBean bean    = new RocketMqMessageBean(String.join("|", String.valueOf(killId), String.valueOf(userId)), null, System.currentTimeMillis());
        Message             message = new GenericMessage(bean);
        //??????????????????????????????????????????????????????????????????????????????redis????????????????????????????????????
        //MySQL???????????????????????????????????????????????????????????????
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("async sendKillSuccessMessage success, killId: {},userId:{}", killId, userId);
            }

            @Override
            public void onException(Throwable e) {
                log.error("async sendKillSuccessMessage onException, killId: {},userId:{}", killId, userId, e);
                //???????????????????????????????????????redis?????????????????????
                redisTemplate.opsForHash().increment(CacheKey.getSeckillHash(String.valueOf(killId)), "count", 1);
                redisTemplate.opsForSet().remove(CacheKey.getSeckillBuyPhones(String.valueOf(killId)), userId);
            }
        });
    }

    private Long decreaseCacheStock(long killId) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("stock.lua")));
        redisScript.setResultType(Long.class);
        Long result = (Long) redisTemplate.execute(redisScript, Lists.newArrayList(CacheKey.getSeckillHash(String.valueOf(killId)), "count"));
        if (result != null && result < 0) {
            throw new NoStockException(String.valueOf(killId));
        }
        return result;
    }

    private void repeatKillCheck(long killId, long userId) {
        if (redisTemplate.opsForSet().isMember(CacheKey.getSeckillBuyPhones(String.valueOf(killId)), userId)) {
            throw new RepeatKillException(String.join("|", String.valueOf(killId), String.valueOf(userId)));
        }
    }

    private void urlCheck(long killId, long userId, String md5) {
        if (md5 == null || !md5.equals(getMD5(killId, userId))) {
            throw new SeckillCloseException(String.join("|", String.valueOf(killId), String.valueOf(userId)));
        }
    }

    @Override
    public void addKill(SeckillDto seckillDto) {
        //TODO
    }
}
