package com.system.design.seckill.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.system.design.seckill.*","com.gitee.sunchenbin.mybatis.actable.manager.*"})
@EnableSwagger2
@MapperScan(basePackages = {"com.system.design.seckill.mapper","com.gitee.sunchenbin.mybatis.actable.dao.*"})
public class SeckillDesignWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillDesignWebApplication.class, args);
    }

}
