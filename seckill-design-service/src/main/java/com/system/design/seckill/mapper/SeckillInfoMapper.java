package com.system.design.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.design.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@Repository
@SuppressWarnings("all")
public interface SeckillInfoMapper extends BaseMapper<Seckill> {
    @Select("select * from t_seckill")
    List<Seckill> selctAll();

}
