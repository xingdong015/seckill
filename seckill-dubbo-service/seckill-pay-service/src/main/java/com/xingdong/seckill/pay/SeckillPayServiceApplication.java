package com.xingdong.seckill.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeckillPayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillPayServiceApplication.class, args);
    }

}
