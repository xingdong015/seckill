package com.system.design.seckill.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@Configurable
public class BeanConfig {

    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }
}
