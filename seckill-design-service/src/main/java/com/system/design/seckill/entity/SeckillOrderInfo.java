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
@TableName("seckill_order_info")
@SuppressWarnings("all")
public class SeckillOrderInfo {
    /**
     * 订单id
     */
    @Id
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long    orderId;

    @TableField(value = "ctime", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long ctime;

    @TableField(value = "utime", fill = UPDATE, jdbcType = JdbcType.DATE)
    private Long utime;

    /**
     * 商品id
     */
    @TableField(value = "product_id", jdbcType = JdbcType.BIGINT)
    private String  productId;
    /**
     * 秒杀id
     */
    @TableField(value = "seckill_id", jdbcType = JdbcType.BIGINT)
    private Long    seckillId;
    /**
     * 用户id
     */
    @TableField(value = "user_id",  jdbcType = JdbcType.BIGINT)
    private String  userId;
    /**
     * '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
     */
    @TableField(value = "status", jdbcType = JdbcType.TINYINT)
    private Integer status;
}
