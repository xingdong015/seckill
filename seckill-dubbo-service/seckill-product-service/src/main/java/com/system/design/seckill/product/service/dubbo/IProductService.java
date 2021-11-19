package com.system.design.seckill.product.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.system.design.seckill.common.dto.ProductDto;
import com.system.design.seckill.common.po.Product;
import com.system.design.seckill.product.service.ProductService;
import com.system.design.seckill.product.utils.CacheKey;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description: 供外部调用的redis操作入口
 * @author: 贾凯
 * @create: 2021-11-18 18:26
 */
//@DubboService(version = "1.0.0")
public class IProductService {
    @Resource
    private ProductService productService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * @Description: 缓存中获取产品信息
     * @param: productId
     * @return com.system.design.seckill.common.po.Product
     * @author jiakai
     * @date 2021/11/19 16:32
    */
    public Product getProductInfo(long productId) {
        String key = CacheKey.getProductHash(String.valueOf(productId));
        if (CollectionUtil.isNotEmpty(redisTemplate.opsForHash().entries(key))){
            return (Product) redisTemplate.opsForHash().entries(key);
        }
        Product productInfo = productService.getProductInfo(productId);
        redisTemplate.opsForHash().putAll(CacheKey.getProductHash(String.valueOf(productId)), BeanUtil.beanToMap(productInfo));
        return productInfo;
    }

    public IPage selectByPage(ProductDto productVo) {
//        String key = CacheKey.getProductHash(String.valueOf(productVo.getId()));
//        Cursor<Map.Entry<Object, Object>> curosr = redisTemplate.opsForHash().scan(key,
//                ScanOptions.NONE);
//        while(curosr.hasNext()){
//            Map.Entry<Object, Object> entry = curosr.next();
//            System.out.println(entry.getKey()+":"+entry.getValue());
//        }
        return productService.selectByPage(productVo);
    }

}
