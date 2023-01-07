package com.xingdong.seckill.common.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xingdong.seckill.common.dto.ProductDto;
import com.xingdong.seckill.common.po.Product;

import java.util.List;

/**
 * @author jack
 * @date 2021/11/8
 */
public interface IProductService {
    /**
     * 创建产品
     * @param product
     * @return
     */
    int createProduct(Product product);

    /**
     * 删除产品
     * @param productId
     * @return
     */
    int deleteProduct(long productId);

    /**
     * 更新产品
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 获取产品详情
     * @param productId
     * @return
     */
    Product getProductInfo(long productId);

    /**
     * 获取所有产品信息
     * @return
     */
    List<Product> getAll();

    /**
     * 分页检索产品
     * @param productVo
     * @return
     */
    IPage selectByPage(ProductDto productVo);
}
