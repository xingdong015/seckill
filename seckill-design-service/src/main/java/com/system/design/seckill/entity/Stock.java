package com.system.design.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

/**
 * @author chengzhengzheng
 * @date 2021/9/20
 */
@SuppressWarnings("all")
@TableName("stock")
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

}
