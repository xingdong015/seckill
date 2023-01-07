package com.xingdong.seckill;

import com.xingdong.seckill.account.config.MyBatisMapperScannerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.xingdong.seckill.account")
@MapperScan(basePackages = {"com.xingdong.seckill.account.mapper"})
@Import({MyBatisMapperScannerConfig.class})
public class SeckillAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillAccountServiceApplication.class, args);
    }

}
