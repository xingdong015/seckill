package com.system.design.seckill.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.system.design.seckill.bean.Exposer;
import com.system.design.seckill.bean.SeckillExecution;
import com.system.design.seckill.bean.SeckillPo;
import com.system.design.seckill.bean.SeckillStatEnum;
import com.system.design.seckill.entity.Seckill;
import com.system.design.seckill.exception.RepeatKillException;
import com.system.design.seckill.exception.SeckillCloseException;
import com.system.design.seckill.exception.SeckillException;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import com.system.design.seckill.utils.RedisKeysConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
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
        final Set<String> members = redisTemplate.opsForZSet().reverseRange(RedisKeysConstant.allSeckillInfo(), 0, -1);
        final List<Object> list = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                for (String member : members) {
                    String key = RedisKeysConstant.getSeckillInfo(member);
                    connection.hGetAll(key.getBytes(StandardCharsets.UTF_8));
                }
                return null;
            }
        });
        final List<SeckillPo> seckillPos = new ArrayList<>();
        list.forEach(map -> {
            Map<String, String> valMap    = (Map<String, String>) map;
            SeckillPo           seckillPo = new SeckillPo();
            seckillPo.setId(Long.parseLong(valMap.get(RedisKeysConstant.STOCK_ID)));
            seckillPo.setCount(Long.parseLong(valMap.get(RedisKeysConstant.STOCK_COUNT)));
            seckillPo.setVersion(valMap.get(RedisKeysConstant.STOCK_VERSION));
            seckillPo.setSale(Long.parseLong(valMap.get(RedisKeysConstant.STOCK_SALE)));
            seckillPo.setName(valMap.get(RedisKeysConstant.STOCK_NAME));
            seckillPos.add(seckillPo);
        });
        return seckillPos;
    }

    @Override
    public SeckillPo getById(String seckillId) throws JsonProcessingException {
        try {
            while (true) {
                List<String> stockInfo = redisTemplate.opsForHash().multiGet(RedisKeysConstant.getSeckillInfo(seckillId),
                        Lists.newArrayList(
                                RedisKeysConstant.STOCK_COUNT,
                                RedisKeysConstant.STOCK_SALE,
                                RedisKeysConstant.STOCK_VERSION,
                                RedisKeysConstant.STOCK_NAME));
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
                    map.put(RedisKeysConstant.STOCK_COUNT, String.valueOf(seckill.getCount()));
                    map.put(RedisKeysConstant.STOCK_SALE, String.valueOf(seckill.getLockCount()));
                    map.put(RedisKeysConstant.STOCK_ID, String.valueOf(seckill.getSeckillId()));
                    map.put(RedisKeysConstant.STOCK_NAME, String.valueOf(seckill.getSeckillName()));
//                    map.put(RedisKeysConstant.STOCK_VERSION, String.valueOf(seckill.getVersion()));
                    redisTemplate.opsForHash().putAll(RedisKeysConstant.getSeckillInfo(seckillId), map);
                    SeckillPo seckillPo = new SeckillPo();
                    seckillPo.setId(Long.parseLong(seckillId));
                    seckillPo.setCount(seckill.getCount());
                    seckillPo.setSale(seckill.getLockCount());
//                    seckillPo.setVersion(String.valueOf(seckill.getVersion()));
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
    public SeckillExecution executeSeckill(long seckillId, long userPhone) throws SeckillException,
            RepeatKillException, SeckillCloseException {
        while (true) {
            //1. 检查库存是否足够 redis中检查库存是否足够、不够直接拒绝

            final List list = redisTemplate.opsForHash().multiGet(RedisKeysConstant.getSeckillInfo(String.valueOf(seckillId)),
                    Lists.newArrayList(RedisKeysConstant.STOCK_COUNT, RedisKeysConstant.STOCK_SALE, RedisKeysConstant.STOCK_VERSION));
            if (CollectionUtils.isNotEmpty(list)) {
                long stockCount   = NumberUtils.toLong(String.valueOf(list.get(0)), 0);
                long stockSale    = NumberUtils.toLong(String.valueOf(list.get(1)), 0);
                long stockVersion = NumberUtils.toLong(String.valueOf(list.get(2)), -1);
                if (stockCount <= stockSale) {
                    return new SeckillExecution(seckillId, SeckillStatEnum.END);
                }
                boolean updateSuccess = seckillInfoMapper.update(seckillId, stockVersion);
                if (updateSuccess) {
                    //3. 扣减库存成功、更新redis中的库存信息、以及 version信息  -----》这里实际上就是先更新数据库和先更新缓存的问题。  https://juejin
                    //.cn/post/6844903604998914055#heading-11

                    //加入执行完数据库的操作之后、系统死掉、那么后续都没有请求了，设置一个过期时间
                    Map<String, String> infos = new HashMap<>();
                    infos.put(RedisKeysConstant.STOCK_SALE, String.valueOf(stockSale + 1));
                    infos.put(RedisKeysConstant.STOCK_VERSION, String.valueOf(stockVersion + 1));
                    redisTemplate.opsForHash().putAll(RedisKeysConstant.getSeckillInfo(String.valueOf(seckillId)), infos);
                    //5. 生成订单信息
                    createOrderInfo(seckillId);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS);
                } else {
                    //4. 扣减库存失败、重试
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignore) {
                    }
                }
            }

        }
    }

    private void createOrderInfo(Long seckillPo) {
        System.out.println("创建订单成功,订单id: " + seckillPo);
    }

}
