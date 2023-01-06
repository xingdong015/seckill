package com.system.design.seckill;

import com.system.design.seckill.account.config.MyBatisMapperScannerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.system.design.seckill.account")
@MapperScan(basePackages = {"com.system.design.seckill.account.mapper"})
@Import({MyBatisMapperScannerConfig.class})
public class SeckillAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillAccountServiceApplication.class, args);
    }

}
