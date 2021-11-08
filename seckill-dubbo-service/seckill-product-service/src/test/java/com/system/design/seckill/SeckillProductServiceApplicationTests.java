package com.system.design.seckill;

import com.alibaba.fastjson.JSON;
import com.system.design.seckill.common.entity.Product;
import com.system.design.seckill.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class SeckillProductServiceApplicationTests {

    @Autowired
    private ProductService productService;

    @Test
    void contextLoads() {
        List<Product> allByLimit = productService.getAllByLimit(null);
        System.out.println(JSON.toJSONString(allByLimit));
    }

}
