package com.system.design.seckill.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.design.seckill.common.api.IProductService;
import com.system.design.seckill.common.entity.Product;
import com.system.design.seckill.mapper.ProductMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-08 18:33
 */
@DubboService(version = "1.0.0")
public class ProductService implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public int createProduct(Product product) {
        System.out.println("创建产品信息成功...");
        return productMapper.insert(product);
    }

    @Override
    public Product getProductInfo(long productId) {
        return productMapper.selectById(productId);
    }

    @Override
    public List<Product> getAllByLimit(Product product) {
        LambdaQueryWrapper<Product> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.like(Product::getProductName , "jack");

        Page<Product> productPage = new Page<>(1 , 2);
        IPage<Product> productIPage = productMapper.selectPage(productPage , userLambdaQueryWrapper);
        System.out.println("总页数： "+productIPage.getPages());
        System.out.println("总记录数： "+productIPage.getTotal());
        productIPage.getRecords().forEach(System.out::println);
        return productIPage.getRecords();
    }
}
