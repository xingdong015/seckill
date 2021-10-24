package com.system.design.seckill.dao;

import com.system.design.seckill.bean.OrderStatusEnum;
import com.system.design.seckill.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
//@Repository
//public class OrderDao {
//    private static final String       INSERT_SQL =
//            "insert t_order (create_time,product_id,seckill_id,user_id,status,update_time) values(?,?,?,?,?,?)";
//    @Autowired
//    private              JdbcTemplate jdbcTemplate;
//
//    public Long createOrder(long skuId, String userId) {
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
//            ps.setLong(1, System.currentTimeMillis());
//            ps.setString(2, String.valueOf(skuId));
//            ps.setString(3, String.valueOf(skuId));
//            ps.setString(4, userId);
//            ps.setLong(5, OrderStatusEnum.INIT.getStatus());
//            ps.setLong(6, System.currentTimeMillis());
//            return ps;
//        }, keyHolder);
//        Long generatedId = keyHolder.getKey().longValue();
//        return generatedId;
//    }
//
//    public Order getOrderInfo(Long orderId) {
//        //TODO
//        return null;
//    }
//}
