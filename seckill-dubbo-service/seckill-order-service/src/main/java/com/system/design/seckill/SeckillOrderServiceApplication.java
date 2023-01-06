package com.system.design.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SeckillOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillOrderServiceApplication.class, args);
    }

}
