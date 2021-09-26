package com.system.design.seckill.productwarmservcie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.system.design.seckill.productwarmservcie.mapper")
public class ProductWarmServcieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductWarmServcieApplication.class, args);
    }

}
