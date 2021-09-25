package com.system.design.seckill.common.transaction.config;

import io.seata.spring.annotation.GlobalTransactionScanner;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class GlobalTransactionScannerBuilder {

    @Bean
    public GlobalTransactionScanner getBean() {
        return new GlobalTransactionScanner("tcc-sample","my_test_tx_group");
    }
}
