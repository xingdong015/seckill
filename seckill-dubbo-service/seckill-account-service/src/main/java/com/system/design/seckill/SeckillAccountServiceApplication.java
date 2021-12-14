package com.system.design.seckill;

import com.system.design.seckill.config.MyBatisMapperScannerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan(basePackages = {"com.system.design.seckill.product.mapper"})
@Import({MyBatisMapperScannerConfig.class})
public class SeckillAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillAccountServiceApplication.class, args);
    }

}
