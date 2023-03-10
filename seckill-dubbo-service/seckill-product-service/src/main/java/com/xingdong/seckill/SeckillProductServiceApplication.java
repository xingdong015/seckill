package com.xingdong.seckill;

import com.xingdong.seckill.product.config.ESClientConfig;
import com.xingdong.seckill.product.config.MyBatisMapperScannerConfig;
import com.xingdong.seckill.product.config.RedisConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan(basePackages = {"com.xingdong.seckill.product.mapper"})
@Import({MyBatisMapperScannerConfig.class, ESClientConfig.class, RedisConfig.class})
@EnableAsync//开启异步调用
public class SeckillProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillProductServiceApplication.class, args);
    }

}
