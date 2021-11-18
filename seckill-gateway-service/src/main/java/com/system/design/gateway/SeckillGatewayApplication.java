package com.system.design.gateway;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan(basePackages = "com.system.design.gateway.gateway")
public class SeckillGatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SeckillGatewayApplication.class).run(args);
    }

}
