package com.system.design.seckill.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.system.design.seckill.bean.Exposer;
import com.system.design.seckill.bean.SeckillResultStatus;
import com.system.design.seckill.exception.RepeatKillException;
import com.system.design.seckill.exception.SeckillCloseException;
import com.system.design.seckill.exception.SeckillException;

import java.util.List;
import java.util.Map;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@SuppressWarnings("all")
public interface SeckillBuzService {

    /**
     * 查询全部的秒杀记录
     * @return
     */
    List<Map<String, Object>> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Map<String, Object> getById(String seckillId) throws JsonProcessingException;


    /**
     * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportKillUrl(long seckillId);


    /**
     * 执行秒杀操作，有可能失败，有可能成功，所以要抛出我们允许的异常
     *
     * @param seckillId
     * @param userPhone
     * @return
     */
    SeckillResultStatus executeKill(long seckillId, long userPhone) ;
}
