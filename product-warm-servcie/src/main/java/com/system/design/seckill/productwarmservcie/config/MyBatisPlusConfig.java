package com.system.design.seckill.productwarmservcie.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-09-26 14:49
 */
@EnableTransactionManagement
@Configuration
@Component
public class MyBatisPlusConfig {
    /**
     * @Description: 注册乐观锁和分页插件
     * @date 2021/9/26 14:50
     */
    @Bean
    public MybatisPlusInterceptor optimisticLockerAndPaginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor()); // 乐观锁插件
        // DbType：数据库类型(根据类型获取应使用的分页方言)
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 分页插件
        return interceptor;
    }
}
