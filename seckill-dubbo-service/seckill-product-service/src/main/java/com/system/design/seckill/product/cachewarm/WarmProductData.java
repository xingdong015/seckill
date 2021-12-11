package com.system.design.seckill.product.cachewarm;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.system.design.seckill.common.po.Product;
import com.system.design.seckill.product.service.ProductService;
import com.system.design.seckill.product.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RedisTemplate  redisTemplate;

    @Override
    public void run(ApplicationArguments args) {
//        delOldProductInfo();
//        cacheNewProduct(productService.getAll());
    }

    private void cacheNewProduct(List<Product> productList) {
        if (productList.isEmpty()) {
            return;
        }
        List<List<Product>> partition = Lists.partition(productList, productList.size() / 10);
        partition.parallelStream().forEach(list -> redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
            list.stream().forEach(product -> {
                redisConnection.zAdd(CacheKey.allProductIdZset().getBytes(), product.getCreateTime(), String.valueOf(product.getId()).getBytes());
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(product);
                Map<byte[], byte[]> values          = new HashMap<>();
                stringObjectMap.forEach((s, o) -> values.put(s.getBytes(StandardCharsets.UTF_8), String.valueOf(o).getBytes(StandardCharsets.UTF_8)));
                redisConnection.hMSet(CacheKey.getProductHash(String.valueOf(product.getId())).getBytes(), values);
            });
            return null;
        }));
    }

    private void delOldProductInfo() {
        List<String>       allProducts       = Lists.newArrayList(redisTemplate.opsForZSet().range(CacheKey.allProductIdZset(), 0, -1));
        List<List<String>> productPartitions = Lists.partition(allProducts, 100);
        productPartitions.parallelStream().forEach(productPartitionIds -> redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
            productPartitionIds.stream().forEach(id -> redisConnection.del(id.getBytes(StandardCharsets.UTF_8)));
            return null;
        }));
        redisTemplate.delete(CacheKey.allProductIdZset());
    }
}
