package com.system.design.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.system.design.seckill.web")
public class SeckillDesignWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillDesignWebApplication.class, args);
    }



}
