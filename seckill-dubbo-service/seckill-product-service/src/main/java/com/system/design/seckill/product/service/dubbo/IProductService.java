package com.system.design.seckill.product.service.dubbo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.design.seckill.common.dto.ProductDto;
import com.system.design.seckill.common.po.Product;
import com.system.design.seckill.product.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @description: 供外部调用的redis操作入口
 * @author: 贾凯
 * @create: 2021-11-18 18:26
 */
@DubboService(version = "1.0.0")
public class IProductService {
    @Resource
    private ProductService productService;

    public Product getProductInfo(long productId) {
        return productService.getProductInfo(productId);
    }

    public IPage selectByPage(ProductDto productVo) {
        return productService.selectByPage(productVo);
    }

}
