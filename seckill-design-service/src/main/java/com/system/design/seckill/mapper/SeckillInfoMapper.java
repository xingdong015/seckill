package com.system.design.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.design.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@Repository
@SuppressWarnings("all")
public interface SeckillInfoMapper extends BaseMapper<Seckill> {

    @Update("UPDATE seckill_info  SET lock_count = lock_count + 1,version = version + 1 WHERE seckill_id = #{seckillId} AND lock_count < count AND version=#{version}")
    boolean update(long seckillId,long version);
    @Select("select * from seckill_info")
    List<Seckill> selctAll();

}
