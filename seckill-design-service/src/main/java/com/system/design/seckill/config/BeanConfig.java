package com.system.design.seckill.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@Configurable
@Component
public class BeanConfig {

    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }

    @Bean
    public Mapper doZerBeanMapper() {
        return new DozerBeanMapper();
    }
}
