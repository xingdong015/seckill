package com.system.design.seckill.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.checkerframework.framework.qual.AnnotatedFor;
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

    @TableField(value = "seckill_name", jdbcType = JdbcType.VARCHAR)
    private String seckillName;

    @TableField(value = "product_id", jdbcType = JdbcType.BIGINT)
    private Long productId;

    @TableField(value = "price", jdbcType = JdbcType.DECIMAL)
    private BigDecimal price;

    @TableField(value = "count", jdbcType = JdbcType.BIGINT)
    private Long count;

    /**
     * tcc事务中使用到的字段、如果是非tcc事务、则不使用
     */
    @TableField(value = "residue", jdbcType = JdbcType.BIGINT)
    private Long residue;

    /**
     * tcc事务中使用到的字段、如果是非tcc事务、则不使用、锁定的库存
     */
    @TableField(value = "frozen", jdbcType = JdbcType.BIGINT)
    private Long frozen;

    @TableField(value = "start_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long startTime;

    @TableField(value = "end_time", fill = INSERT_UPDATE, jdbcType = JdbcType.DATE)
    private Long endTime;

    @TableField(value = "create_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long createTime;

    @TableField(value = "update_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long updateTime;


}
