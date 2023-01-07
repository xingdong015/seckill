package com.xingdong.seckill.product.utils;

/**
 * @Description:  商品信息t_product的redis缓存key定义
 * @author jiakai
 * @date 2021/11/19 16:04
*/
public interface CacheKey{

    /**
     * 所有的商品id列表
     * zset类型、按照创建时间排序
     *
     * @return
     */
    static String allProductIdZset() {
        return "product:all:list";
    }

    /**
     * 单个商品信息
     * 详情页面中展示的信息
     * hash类型: 对应Product数据库信息
     *
     * @param id
     * @return
     */
    static String getProductHash(String id) {
        return String.format("product:%s:info", id);
    }

}
