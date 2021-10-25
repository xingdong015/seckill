package com.system.design.seckill.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chengzhengzheng
 * @date 2021/10/24
 */
@Mapper
public interface SeckillMapper extends BaseMapper<Seckill> {

    Integer decreaseStorage(Long killId);
}
