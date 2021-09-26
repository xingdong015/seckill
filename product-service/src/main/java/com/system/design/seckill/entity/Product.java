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

@Data
@TableName("t_product")
public class Product {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "product_name", jdbcType = JdbcType.VARCHAR)
    private String productName;

    @TableField(value = "product_desc", jdbcType = JdbcType.VARCHAR)
    private String productDesc;

    @TableField(value = "price", jdbcType = JdbcType.DECIMAL)
    private BigDecimal price;

    @TableField(value = "create_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long createTime;

    @TableField(value = "update_time", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long updateTime;
}
