package com.system.design.seckill.product.common.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVo {
    private Long currentPage;
    private Long pageSize;
    private Long id;
    private String productName;
    private String productDesc;
    private BigDecimal price;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Long createTimeStart;
    private Long createTimeEnd;
    private Long updateTimeStart;
    private Long updateTimeEnd;
}
