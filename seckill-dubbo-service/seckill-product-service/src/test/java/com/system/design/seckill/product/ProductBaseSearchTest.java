package com.system.design.seckill.product;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.system.design.seckill.SeckillProductServiceApplicationTests;
import com.system.design.seckill.common.po.Product;
import com.system.design.seckill.common.dto.ProductDto;
import com.system.design.seckill.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-08 19:31
 */
@Slf4j
public class ProductBaseSearchTest extends SeckillProductServiceApplicationTests {
    @Resource
    private ProductService productService;

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(8550);
        System.out.println(bigDecimal);
    }

    @Test
    void test_insert(){
        Product build = Product.builder()
                .id(1L)
                .price(new BigDecimal(8550).setScale(5, RoundingMode.DOWN))
                .productName("xiaomi-pro")
                .productDesc("big computer")
                .build();

        int insert = productService.createProduct(build);
        System.out.println("insert: " + insert);
    }

    @Test
    void test_delete(){
        int insert = productService.deleteProduct(1L);
        System.out.println("delete: " + insert);
    }

    @Test
    void test_update(){
        Product build = Product.builder()
                .id(1L)
                .price(new BigDecimal(9000))
                .productName("v3-huawei-pro")
                .productDesc("v3-small computer")
                .build();
        int insert = productService.updateProduct(build);
        System.out.println("update: " + insert);
    }

    @Test
    void test_query(){
        ProductDto build = ProductDto.builder()
                .minPrice(new BigDecimal(1000))
                .maxPrice(new BigDecimal(15000))
                .build();
        IPage iPage = productService.selectByPage(build);
        System.out.println(JSON.toJSONString(iPage.getRecords()));
    }
}
