package com.system.design.seckill.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.system.design.seckill.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {


    @Select(value = "select * from Order where id = ?1")
    Order findOrder(@Param(value = "id") Long id);


}
