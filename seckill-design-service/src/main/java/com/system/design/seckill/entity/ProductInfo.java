package com.system.design.seckill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.UPDATE;

@Data
@TableName("product_info")
public class ProductInfo {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "ctime", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long ctime;

    @TableField(value = "utime", fill = UPDATE, jdbcType = JdbcType.DATE)
    private Long utime;

    @TableField(value = "product_name", jdbcType = JdbcType.VARCHAR)
    private String productName;

    @TableField(value = "product_desc", jdbcType = JdbcType.VARCHAR)
    private String productDesc;

    @TableField(value = "price", jdbcType = JdbcType.DECIMAL)
    private BigDecimal price;
}
