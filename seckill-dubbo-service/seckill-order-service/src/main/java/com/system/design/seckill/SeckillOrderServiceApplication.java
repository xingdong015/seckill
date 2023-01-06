package com.system.design.seckill;

import com.system.design.seckill.order.config.MyBatisMapperScannerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


//这里的 com.gitee.sunchenbin.mybatis.actable.dao.*  是为了自动生成表结构来使用的、开发完成之后可以删除
@MapperScan(basePackages = {"com.system.design.seckill.order.mapper","com.gitee.sunchenbin.mybatis.actable.dao.*"})
@Import(MyBatisMapperScannerConfig.class)
@SpringBootApplication(scanBasePackages =
        {"com.gitee.sunchenbin.mybatis.actable.manager.*", "com.system.design.seckill.order.*"})
public class SeckillOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillOrderServiceApplication.class, args);
    }

}
