package com.system.design.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.system.design.seckill.order.mapper"})
public class SeckillOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillOrderServiceApplication.class, args);
    }

}
