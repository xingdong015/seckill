package com.system.design.seckill.dbservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.design.seckill.entity.Seckill;
import com.system.design.seckill.mapper.SeckillInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
public class SeckillService extends ServiceImpl<SeckillInfoMapper, Seckill> {

    public Boolean deductStock(Long killId) {
        return true;
    }

    public void incCount(long seckillId, int count) {

    }
}
