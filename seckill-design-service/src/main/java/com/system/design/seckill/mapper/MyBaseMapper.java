package com.system.design.seckill.mapper;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.system.design.seckill.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class MyBaseMapper extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
