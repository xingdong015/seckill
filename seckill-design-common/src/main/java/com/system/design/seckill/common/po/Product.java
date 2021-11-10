package com.system.design.seckill.common.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @TableField(value = "create_time", fill = INSERT, jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;

    @TableField(value = "update_time", fill = INSERT_UPDATE, jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime;

    /**
     * 版本号（用于乐观锁， 默认为 1）
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
