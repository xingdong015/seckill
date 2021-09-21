package com.system.design.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.UPDATE;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@SuppressWarnings("all")
@TableName("t_user")
public class User {
    @Id
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField(value = "phone", jdbcType = JdbcType.VARCHAR)
    private String phone;

    @TableField(value = "address", jdbcType = JdbcType.VARCHAR)
    private String address;

    @TableField(value = "email", jdbcType = JdbcType.VARCHAR)
    private String email;

    @TableField(value = "ip_address", jdbcType = JdbcType.VARCHAR)
    private String ipAddress;

    @TableField(value = "create_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long createTime;

    @TableField(value = "update_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long updateTime;

}
