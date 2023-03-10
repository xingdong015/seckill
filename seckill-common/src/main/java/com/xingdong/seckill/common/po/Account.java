package com.xingdong.seckill.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@SuppressWarnings("all")
@Data
@TableName("t_account")
public class Account implements Serializable {
    @Id
    @TableId(value = "account_id", type = IdType.AUTO)
    private Long accountId;

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
