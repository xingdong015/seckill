package com.system.design.seckill.service;

import com.system.design.seckill.entity.ProductInfo;
import com.system.design.seckill.entity.Seckill;
import com.system.design.seckill.mapper.ProductInfoMapper;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import com.system.design.seckill.utils.CacheKey;
import org.apache.commons.collections4.CollectionUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
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
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private RedisTemplate     redisTemplate;
    @Autowired
    private Mapper            doZerBeanMapper;

    @PostConstruct
    public void init() {
        mockData();
        doInit();
    }

    private void mockData() {
        mockProduct();
        mockSeckillInfo();
    }

    private void mockSeckillInfo() {
        Seckill seckill = new Seckill();
        seckill.setSeckillName("iphone13 秒杀");
        seckill.setCreateTime(System.currentTimeMillis());
        seckill.setStartTime(System.currentTimeMillis());
        seckill.setEndTime(System.currentTimeMillis() + 10 * 3600 * 1000 * 24);
        seckill.setCount(1000L);
        seckill.setUpdateTime(System.currentTimeMillis());
        seckill.setProductId(1L);
        seckill.setSaleCount(0L);
        seckillInfoMapper.insert(seckill);

        Seckill seckill1 = new Seckill();
        seckill1.setSeckillName("iphone12 秒杀");
        seckill1.setCreateTime(System.currentTimeMillis());
        seckill1.setStartTime(System.currentTimeMillis());
        seckill1.setEndTime(System.currentTimeMillis() + 10 * 3600 * 1000 * 24);
        seckill1.setCount(2000L);
        seckill1.setUpdateTime(System.currentTimeMillis());
        seckill1.setProductId(2L);
        seckill1.setSaleCount(0L);
        seckillInfoMapper.insert(seckill1);
    }

    private void mockProduct() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductDesc("iphone13真香");
        productInfo.setProductName("iphone13");
        productInfo.setPrice(new BigDecimal(6999));
        productInfo.setCreateTime(System.currentTimeMillis());
        productInfo.setUpdateTime(System.currentTimeMillis());
        productInfoMapper.insert(productInfo);

        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setProductDesc("iphone12 也真香");
        productInfo1.setProductName("iphone12");
        productInfo1.setPrice(new BigDecimal(2999));
        productInfo1.setCreateTime(System.currentTimeMillis());
        productInfo1.setUpdateTime(System.currentTimeMillis());
        productInfoMapper.insert(productInfo1);
    }

    private void doInit() {
        redisTemplate.delete(CacheKey.allSeckillIdZset());
        Set members = redisTemplate.opsForZSet().range(CacheKey.allSeckillIdZset(), 0, -1);
        if (CollectionUtils.isEmpty(members)) {
            final List<Seckill> infoList = seckillInfoMapper.selctAll();
            if (CollectionUtils.isNotEmpty(infoList)) {
                Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
                for (Seckill seckill : infoList) {
                    tuples.add(ZSetOperations.TypedTuple.of(String.valueOf(seckill.getSeckillId()), (double) seckill.getStartTime()));
                    String key = CacheKey.getSeckillHash(String.valueOf(seckill.getSeckillId()));
                    redisTemplate.delete(key);
                    redisTemplate.opsForHash().putAll(key, doZerBeanMapper.map(seckill, Map.class));
                }
                redisTemplate.opsForZSet().add(CacheKey.allSeckillIdZset(), tuples);
            }
        }
    }
}
