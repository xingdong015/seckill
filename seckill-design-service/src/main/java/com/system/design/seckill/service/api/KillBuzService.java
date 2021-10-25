package com.system.design.seckill.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.system.design.seckill.common.bean.Exposer;
import com.system.design.seckill.common.bean.SeckillResultStatus;

import java.util.List;
import java.util.Map;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@SuppressWarnings("all")
public interface KillBuzService {

    /**
     * 查询全部的秒杀商品
     *
     * @return
     */
    List<Map<String, Object>> getSeckillList();

    /**
     * 查询单个秒杀商品
     *
     * @param seckillId
     * @return
     */
    Map<String, Object> getById(String seckillId) throws JsonProcessingException;


    /**
     * 秒杀开始的时候的秒杀地址信息
     *
     * @param seckillId
     */
    Exposer exportKillUrl(long seckillId,long userId);


    /**
     * 执行秒杀操作
     *
     * @param seckillId
     * @param userPhone
     * @return
     */
    SeckillResultStatus executeKill(long seckillId, long userPhone,String md5) ;
}
