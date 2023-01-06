package com.system.design.seckill.product.redis;

import cn.hutool.core.bean.BeanUtil;
import com.system.design.seckill.SeckillProductServiceApplicationTests;
import com.system.design.seckill.common.po.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-12-08 14:43
 */
public class ProductCacheTest extends SeckillProductServiceApplicationTests {
    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test() {
        Product build = Product.builder().id(1l).productDesc("qwe").productName("test").build();
        redisTemplate.opsForHash().putAll("mu-key", BeanUtil.beanToMap(build));

        List values = redisTemplate.opsForHash().values("mu-key");

        Product build2 = Product.builder().id(1l).productDesc("qwe666").productName("test666").build();
        redisTemplate.opsForHash().putAll("mu-key", BeanUtil.beanToMap(build2));
        List values2 = redisTemplate.opsForHash().values("mu-key");

        System.out.println("end...");
    }
}
