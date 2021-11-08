package com.system.design.seckill.common.utils;

/**
 * @author chengzhengzheng
 * @date 2021/9/18
 */
public interface CacheKey {

    interface StockInfo {
        String STOCK_COUNT = "sock_count";
        String STOCK_ID    = "stock_id";
        String STOCK_NAME  = "stock_name";
        String START_TIME  = "startTime";
        String END_TIME    = "endTime";
    }


    /**
     * 所有的秒杀商品id列表
     * zset类型、按照创建时间排序
     *
     * @return
     */
    static String allSeckillIdZset() {
        return "seck:all:list";
    }

    /**
     * 单个秒杀商品相亲信息
     * 详情页面中展示的信息
     * hash类型: 对应Seckill数据库信息
     *
     * @param id
     * @return
     */
    static String getSeckillHash(String id) {
        return String.format("seck:%s:info", id);
    }

    /**
     * 用户已经抢购过的商品
     * @param id
     * @return
     */
    static String getSeckillBuyPhones(String id) {
        return String.format("seck:%s:buy:ids", id);
    }
}
