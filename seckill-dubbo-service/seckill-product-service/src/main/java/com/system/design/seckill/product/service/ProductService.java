package com.system.design.seckill.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.system.design.seckill.common.api.IProductService;
import com.system.design.seckill.common.po.Product;
import com.system.design.seckill.common.dto.ProductDto;
import com.system.design.seckill.product.config.RedisConfig;
import com.system.design.seckill.product.mapper.ProductMapper;
import com.system.design.seckill.product.utils.BloomFilterUtils;
import com.system.design.seckill.product.utils.CacheKey;
import com.system.design.seckill.product.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-08 18:33
 */
@DubboService(version = "1.0.0")
public class ProductService implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedisConfig redisConfig;
    private static final int EXPIRE_TIME = 5000;
    private static final int SLEEP_VALUE = 50;

    /**
     * @Description: 获取产品详情：
     * 1.bloomFilter前置过滤productId；
     * 2.缓存中不存在详情时，互斥锁从mysql获取填充redis，返回详情
     * @author jiakai
     * @date 2021/11/19 16:32
     */
    public Product getProductFromCache(long productId) {
        // 判断是否为合法id
        boolean mightContain = BloomFilterUtils.mightContain(String.valueOf(productId));
        if (!mightContain){
            return null;
        }
        // 从缓存中获取数据
        String key = CacheKey.getProductHash(String.valueOf(productId));
        Product productInfo = (Product) redisTemplate.opsForHash().entries(key);
        if (productInfo == null) {
            productInfo = getProductAndSetCache(productId, key, productInfo);
        }
        return productInfo;
    }

    private Product getProductAndSetCache(long productId, String key, Product productInfo) {
        Jedis jedis = null;
        String value = UUID.randomUUID().toString();
        try {
            jedis = redisConfig.jedisPoolFactory().getResource();
            boolean tryGetDistributedLock = RedisUtils.tryGetDistributedLock(jedis, key, value, EXPIRE_TIME);
            if (tryGetDistributedLock) {
                productInfo = getProductInfo(productId);
                redisTemplate.opsForHash().putAll(CacheKey.getProductHash(String.valueOf(productId)), BeanUtil.beanToMap(productInfo));
            } else {
                Thread.sleep(SLEEP_VALUE);
                getProductFromCache(productId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                RedisUtils.releaseDistributedLock(jedis, key, value);
                jedis.close();
            }
        }
        return productInfo;
    }

    @Override
    public int createProduct(Product product) {
        int insert = productMapper.insert(product);
        System.out.println("创建产品信息成功: " + insert);
        return insert;
    }

    @Override
    public int deleteProduct(long productId) {
        int delete = productMapper.deleteById(productId);
        return delete;
    }

    @Override
    public int updateProduct(Product product) {
        int update = productMapper.updateById(product);
        return update;
    }

    @Override
    public Product getProductInfo(long productId) {
        return productMapper.selectById(productId);
    }

    @Override
    public List<Product> getAll() {
        return productMapper.selectList(null);
    }

    @Override
    public IPage selectByPage(ProductDto productVo) {
        Page<Product> productPage = new Page<>();
        LambdaQueryWrapper<Product> productLambdaQueryWrapper = Wrappers.lambdaQuery();
        if (productVo != null){
            if (productVo.getCurrentPage() != null){productPage.setCurrent(productVo.getCurrentPage());}
            if (productVo.getPageSize() != null){productPage.setSize(productVo.getPageSize());}
            if (StringUtils.isNotEmpty(productVo.getProductName())){productLambdaQueryWrapper.like(Product::getProductName, productVo.getProductName());}
            if (StringUtils.isNotEmpty(productVo.getProductDesc())){productLambdaQueryWrapper.like(Product::getProductDesc, productVo.getProductDesc());}
            if (productVo.getId() != null){productLambdaQueryWrapper.eq(Product::getId, productVo.getId());}
            if (productVo.getCreateTimeStart() != null){productLambdaQueryWrapper.ge(Product::getCreateTime,productVo.getCreateTimeStart());}
            if (productVo.getCreateTimeEnd() != null){productLambdaQueryWrapper.le(Product::getCreateTime,productVo.getCreateTimeEnd());}
            if (productVo.getUpdateTimeStart() != null){productLambdaQueryWrapper.ge(Product::getUpdateTime,productVo.getUpdateTimeStart());}
            if (productVo.getUpdateTimeEnd() != null){productLambdaQueryWrapper.le(Product::getUpdateTime,productVo.getUpdateTimeEnd());}
            if (productVo.getPrice() != null){productLambdaQueryWrapper.eq(Product::getPrice,productVo.getPrice());}
            if (productVo.getMinPrice() != null){productLambdaQueryWrapper.ge(Product::getPrice,productVo.getMinPrice());}
            if (productVo.getMaxPrice() != null){productLambdaQueryWrapper.le(Product::getPrice,productVo.getMaxPrice());}
        }
        return productMapper.selectPage(productPage , productLambdaQueryWrapper);
    }
}
