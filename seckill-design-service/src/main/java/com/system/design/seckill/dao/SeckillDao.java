//package com.system.design.seckill.dao;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementCreator;
//import org.springframework.stereotype.Repository;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
///**
// * @author chengzhengzheng
// * @date 2021/10/23
// */
//@Repository
//public class SeckillDao {
//    @Autowired
//    private              JdbcTemplate jdbcTemplate;
//    private static final String       UPDATE_SQL = "update t_seckill set count = count - 1 where seckill_id = ? and count > 0";
//
//    public Integer updateStock(long seckill) {
//        return jdbcTemplate.update(connection -> {
//            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
//            preparedStatement.setString(1, String.valueOf(seckill));
//            preparedStatement.executeUpdate();
//            return preparedStatement;
//        });
//    }
//}
