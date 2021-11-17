package com.system.design.seckill.product.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.design.seckill.common.api.IProductService;
import com.system.design.seckill.common.po.Product;
import com.system.design.seckill.common.dto.ProductDto;
import com.system.design.seckill.product.mapper.ProductMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

    //****暂时curd都加redis
//    @GlobalTransactional
    @Override
    public int createProduct(Product product) {
        int insert = productMapper.insert(product);
//        String key = "product:id:" + product.getId();
//        redisTemplate.opsForZSet().add(key, JSON.toJSON(product), product.getId());
        System.out.println("创建产品信息成功: " + insert);
        return insert;
    }

    @Override
    public int deleteProduct(long productId) {
        int delete = productMapper.deleteById(productId);
//        String key = "product:id:" + productId;
//        redisTemplate.opsForZSet().removeRange(key, productId, productId);
        return delete;
    }

    @Override
    public int updateProduct(Product product) {
//        String key = "product:id:" + product.getId();
//        redisTemplate.opsForZSet().removeRange(key, product.getId(), product.getId());
//        redisTemplate.opsForZSet().add(key, JSON.toJSON(product), product.getId());
        int update = productMapper.updateById(product);
        return update;
    }

    @Override
    public Product getProductInfo(long productId) {
//        redisTemplate.opsForZSet().scan()
        return productMapper.selectById(productId);
    }

    @Override
    public IPage selectByPage(ProductDto productVo) {
        //zset 结构分页检索

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
