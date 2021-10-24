package com.system.design.seckill.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.system.design.seckill.entity.Order;
import com.system.design.seckill.mapper.OrderMapper;
import com.system.design.seckill.service.api.OrderService;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
