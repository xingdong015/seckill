package com.system.design.seckill.utils;

/**
 * @author chengzhengzheng
 * @date 2021/9/18
 */
public interface RedisKeysWrapper {

    interface StockInfo{
        String STOCK_COUNT   = "sock_count";
        String STOCK_ID      = "stock_id";
        String STOCK_NAME    = "stock_name";
    }


    /**
     * 所有的秒杀商品id列表
     * zset类型、按照创建时间排序
     *
     * @return
     */
    public static String allSeckillIdZset() {
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
    public static String getSeckillHash(String id) {
        return String.format("seck:%s:info", id);
    }


    public static String getSeckillInfoLock(long seckillId) {
        return null;
    }
}
