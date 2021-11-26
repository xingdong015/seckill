package com.system.design.seckill.product.cachewarm;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Collections2;
import com.system.design.seckill.common.po.Product;
import com.system.design.seckill.product.service.ProductService;
import com.system.design.seckill.product.utils.CacheKey;
import com.system.design.seckill.product.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @description: 初始化预热t_product表数据
 * @author: 贾凯
 * @create: 2021-11-26 10:10
 */
@Component
@Slf4j
public class WarmProductData implements ApplicationRunner {
    @Resource
    private ProductService productService;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            //1.获取产品表中数据
            List<Product> productList = productService.getAll();
            if (CollectionUtils.isEmpty(productList)) {
                log.info("t_product表没有数据，无缓存数据");
                return;
            }
            //2.缓存到redis：id->zset; data->hash
            Long count = redisTemplate.opsForZSet().removeRange(CacheKey.allProductIdZset(), 0, -1);
            log.info("zset清空product数据共 {} 个", count);
            //???如何清空hash中的product信息
            productList.stream().forEach(product -> {
                long id = product.getId();
                long createTime = product.getCreateTime();
                redisTemplate.opsForZSet().add(CacheKey.allProductIdZset(), CacheKey.getProductHash(String.valueOf(id)), createTime);
                redisTemplate.opsForHash().putAll(CacheKey.getProductHash(String.valueOf(id)), BeanUtil.beanToMap(product));
            });
        }catch (Exception e){
            log.error("product缓存预热异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
