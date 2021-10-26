package com.system.design.seckill.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.system.design.seckill.common.entity.OrderEntity;
import com.system.design.seckill.db.mapper.OrderMapper;
import com.system.design.seckill.service.api.OrderService;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {

}
