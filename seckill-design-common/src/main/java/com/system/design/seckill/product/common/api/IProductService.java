package com.system.design.seckill.product.common.api;

import com.system.design.seckill.product.common.entity.Product;

import java.util.List;

/**
 * @author jack
 * @date 2021/11/8
 */
public interface IProductService {
    /**
     * 创建订单
     * @param product
     * @return
     */
    int createProduct(Product product);

    /**
     * 获取产品详情
     * @param productId
     * @return
     */
    Product getProductInfo(long productId);

    /**
     * 分页检索产品
     * @param product
     * @return
     */
    List<Product> getAllByLimit(Product product);
}
