package com.system.design.seckill.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.system.design.seckill.bean.Exposer;
import com.system.design.seckill.bean.SeckillPo;
import com.system.design.seckill.bean.SeckillResultStatus;
import com.system.design.seckill.entity.Seckill;
import com.system.design.seckill.exception.RepeatKillException;
import com.system.design.seckill.exception.SeckillCloseException;
import com.system.design.seckill.exception.SeckillException;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import com.system.design.seckill.utils.RedisKeysWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
@SuppressWarnings("all")
@Service
public class SeckillServiceImpl extends ServiceImpl<SeckillInfoMapper, Seckill> implements SeckillBuzService {

    @Autowired
    private RedisTemplate     redisTemplate;
    @Autowired
    private ObjectMapper      mapper;
    @Autowired
    private SeckillInfoMapper seckillInfoMapper;


    @Override
    public List<SeckillPo> getSeckillList() {
        //获取所有的秒杀id
        final Set<String> members = redisTemplate.opsForZSet().reverseRange(RedisKeysWrapper.allSeckillIdZset(), 0, -1);
        final List<Object> list = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                for (String member : members) {
                    String key = RedisKeysWrapper.getSeckillHash(member);
                    connection.hGetAll(key.getBytes(StandardCharsets.UTF_8));
                }
                return null;
            }
        });
        final List<SeckillPo> seckillPos = new ArrayList<>();
        list.forEach(map -> {
            Map<String, String> valMap    = (Map<String, String>) map;
            SeckillPo           seckillPo = new SeckillPo();
            seckillPo.setId(Long.parseLong(valMap.get(RedisKeysWrapper.StockInfo.STOCK_ID)));
            seckillPo.setCount(Long.parseLong(valMap.get(RedisKeysWrapper.StockInfo.STOCK_COUNT)));
            seckillPo.setName(valMap.get(RedisKeysWrapper.StockInfo.STOCK_NAME));
            seckillPos.add(seckillPo);
        });
        return seckillPos;
    }

    @Override
    public SeckillPo getById(String seckillId) throws JsonProcessingException {
        try {
            while (true) {
                List<String> stockInfo = redisTemplate.opsForHash().multiGet(RedisKeysWrapper.getSeckillHash(seckillId),
                        Lists.newArrayList(
                                RedisKeysWrapper.StockInfo.STOCK_COUNT,
                                RedisKeysWrapper.StockInfo.STOCK_NAME));
                stockInfo = stockInfo.stream().filter(Objects::nonNull).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(stockInfo)) {
                    SeckillPo seckillPo = new SeckillPo();
                    seckillPo.setId(Long.parseLong(seckillId));
                    seckillPo.setCount(Integer.valueOf(stockInfo.get(0)));
                    seckillPo.setSale(Integer.valueOf(stockInfo.get(1)));
                    seckillPo.setVersion(stockInfo.get(2));
                    seckillPo.setName(stockInfo.get(3));
                    return seckillPo;
                }
                boolean getLock = getLock(seckillId);
                if (getLock) {
                    Seckill             seckill = seckillInfoMapper.selectById(seckillId);
                    Map<String, String> map     = new HashMap<>();
                    map.put(RedisKeysWrapper.StockInfo.STOCK_COUNT, String.valueOf(seckill.getCount()));
                    map.put(RedisKeysWrapper.StockInfo.STOCK_ID, String.valueOf(seckill.getSeckillId()));
                    map.put(RedisKeysWrapper.StockInfo.STOCK_NAME, String.valueOf(seckill.getSeckillName()));
                    redisTemplate.opsForHash().putAll(RedisKeysWrapper.getSeckillHash(seckillId), map);
                    SeckillPo seckillPo = new SeckillPo();
                    seckillPo.setId(Long.parseLong(seckillId));
                    seckillPo.setCount(seckill.getCount());
                    seckillPo.setSale(seckill.getLockCount());
                    return seckillPo;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        } finally {
            releaseLock(seckillId);
        }
        return null;
    }

    private void releaseLock(String seckillId) {
        System.out.println("成功释放分布式锁: " + seckillId);
    }

    private boolean getLock(String seckillId) {
        System.out.println("成功获取分布式锁: " + seckillId);
        return true;
    }


    @Override
    public Exposer exportSeckillUrl(long seckillId) {

        return null;
    }

    /**
     * 开始秒杀活动
     *
     * @param seckillId
     * @param userPhone
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    @Override
    public SeckillResultStatus executeSeckill(long seckillId, long userPhone) throws SeckillException,
            RepeatKillException, SeckillCloseException {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("stock.lua")));
        redisScript.setResultType(Long.class);
        //1. redis扣减库存
        Long result = (Long) redisTemplate.execute(redisScript, Lists.newArrayList(RedisKeysWrapper.getSeckillHash(String.valueOf(seckillId)), "count"));
        if (result < 0) {
            //3. 扣减失败、返回已经抢空给端上
            return SeckillResultStatus.buildFailureExecute(seckillId);
        }
        //2. 扣减成功、发送mq消息
        return SeckillResultStatus.buildSuccessExecute(seckillId,result);
    }

    private void createOrderInfo(Long seckillPo) {
        System.out.println("创建订单成功,订单id: " + seckillPo);
    }

}
