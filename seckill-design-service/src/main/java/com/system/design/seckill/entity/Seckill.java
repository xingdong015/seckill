package com.system.design.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@SuppressWarnings("all")
@TableName("t_seckill")
@Data
public class Seckill {
    @Id
    @TableId(value = "seckill_id", type = IdType.AUTO)
    private Long seckillId;

    @TableField(value = "start_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long startTime;

    @TableField(value = "end_time", fill = INSERT_UPDATE, jdbcType = JdbcType.DATE)
    private Long endTime;

    @TableField(value = "seckill_name", jdbcType = JdbcType.VARCHAR)
    private String seckillName;

    @TableField(value = "product_id", jdbcType = JdbcType.BIGINT)
    private Long productId;

    @TableField(value = "price", jdbcType = JdbcType.DECIMAL)
    private BigDecimal price;

    @TableField(value = "count", jdbcType = JdbcType.BIGINT)
    private Long count;

    @TableField(value = "sale_count", jdbcType = JdbcType.BIGINT)
    private Long saleCount;

    @TableField(value = "create_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long createTime;

    @TableField(value = "update_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long updateTime;

}
