package com.system.design.seckill.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.design.seckill.entity.ProductInfo;
import com.system.design.seckill.mapper.ProductInfoMapper;
import org.apache.tomcat.jni.Status;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CommodityInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> {

    public String saveProduct() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("iphone 13");
        productInfo.setProductDesc("苹果13");
        productInfo.setPrice(new BigDecimal(59999));
        save(productInfo);
        return Status.APR_OS_START_ERROR + "";
    }
}
