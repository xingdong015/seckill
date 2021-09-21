package com.system.design.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;

/**
 * @author chengzhengzheng
 * @date 2021/9/20
 */
@SuppressWarnings("all")
@TableName("t_stock")
@Data
public class Stock {
    @Id
    @TableId(value = "stock_id", type = IdType.AUTO)
    private Long stockId;

    @TableField(value = "product_id", jdbcType = JdbcType.BIGINT)
    private Long productId;

    @TableField(value = "seckill_id", jdbcType = JdbcType.BIGINT)
    private Long seckillId;

    @TableField(value = "stock_count", jdbcType = JdbcType.BIGINT)
    private Long stockCount;

    @TableField(value = "stock_lock_count", jdbcType = JdbcType.BIGINT)
    private Long lockCount;

    @TableField(value = "create_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long createTime;

    @TableField(value = "update_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long updateTime;

}
