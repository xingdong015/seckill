package com.system.design.seckill.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long currentPage;
    private Long pageSize;
    private Long id;
    private String keyword;
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
