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
@TableName("order")
@SuppressWarnings("all")
public class Order {
    @Id
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long    orderId;

    @TableField(value = "ctime", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long createTime;

    @TableField(value = "product_id", jdbcType = JdbcType.BIGINT)
    private String  productId;

    @TableField(value = "seckill_id", jdbcType = JdbcType.BIGINT)
    private Long    seckillId;

    @TableField(value = "user_id",  jdbcType = JdbcType.BIGINT)
    private String  userId;
    /**
     * '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
     */
    @TableField(value = "status", jdbcType = JdbcType.TINYINT)
    private Integer status;
}
