package com.system.design.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

/**
 * @author tukangzheng001
 * @date 2021/9/26
 */
@TableName("t_stock")
@SuppressWarnings("all")
@Data
public class Stock {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "product_id", jdbcType = JdbcType.BIGINT)
    private Long productId;

    @TableField(value = "seckill_id", jdbcType = JdbcType.BIGINT)
    private Long seckillId;

    @TableField(value = "stock", jdbcType = JdbcType.BIGINT)
    private Long stock;

    @TableField(value = "status", jdbcType = JdbcType.TINYINT)
    private Integer lock;

}
