package com.system.design.seckill.product.common.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @author chengzhengzheng
 * @date 2021/9/18
 */
@Builder
@Data
public class Stock {
    /**
     * 商品id
     */
    private String  id;
    /**
     * 库存
     */
    private Integer count;
    /**
     * 销量
     */
    private Integer sale;
    /**
     * 并发修改版本
     */
    private Integer version;
    /**
     * 商品名称
     */
    private String  name;
}
