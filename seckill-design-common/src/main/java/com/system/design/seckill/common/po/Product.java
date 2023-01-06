package com.system.design.seckill.common.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
    @JsonProperty(value = "product_name")
    private String productName;

    @TableField(value = "product_desc", jdbcType = JdbcType.VARCHAR)
    @JsonProperty(value = "product_desc")
    private String productDesc;

    @TableField(value = "price", jdbcType = JdbcType.DECIMAL)
    private BigDecimal price;

    @TableField(value = "create_time", fill = INSERT, jdbcType = JdbcType.BIGINT)
    @JsonProperty(value = "create_time")
    private Long createTime;

    @TableField(value = "update_time", fill = INSERT_UPDATE, jdbcType = JdbcType.BIGINT)
    @JsonProperty(value = "update_time")
    private Long updateTime;
//
//    public static void main(String[] args) {
//        BigDecimal bigDecimal = new BigDecimal(112.334121).setScale(1, RoundingMode.DOWN);
//        System.out.println(bigDecimal.toString());
//
//        BigInteger bigInteger = new BigInteger("112.3341209999999961155481287278234958648681640625");
//
//
//    }



}
