package com.xingdong.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.xingdong.seckill.web")
public class SeckillDesignWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillDesignWebApplication.class, args);
    }



}
