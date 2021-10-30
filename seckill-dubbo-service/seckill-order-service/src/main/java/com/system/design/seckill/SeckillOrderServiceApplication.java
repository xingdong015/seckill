package com.system.design.seckill;

import com.system.design.seckill.common.config.MyBatisMapperScannerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan(basePackages = {"com.system.design.seckill.order.mapper"})
@Import(MyBatisMapperScannerConfig.class)
public class SeckillOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillOrderServiceApplication.class, args);
    }

}
