package com.system.design.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.system.design.seckill.db.mapper")
public class SeckillDesignWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillDesignWebApplication.class, args);
    }

}