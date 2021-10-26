package com.system.design.seckill.db.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.system.design.seckill.common.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {


    @Select(value = "select * from Order where id = ?1")
    OrderEntity findOrder(@Param(value = "id") Long id);


}
