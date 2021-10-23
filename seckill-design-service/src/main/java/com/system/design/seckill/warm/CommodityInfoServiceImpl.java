//package com.system.design.seckill.service;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.system.design.seckill.entity.Product;
//import com.system.design.seckill.mapper.ProductInfoMapper;
//import org.apache.tomcat.jni.Status;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//
//@Service
//public class CommodityInfoServiceImpl extends ServiceImpl<ProductInfoMapper, Product> {
//
//    public String saveProduct() {
//        Product product = new Product();
//        product.setProductName("iphone 13");
//        product.setProductDesc("苹果13");
//        product.setPrice(new BigDecimal(59999));
//        save(product);
//        return Status.APR_OS_START_ERROR + "";
//    }
//}
