package com.system.design.seckill.utils;

/**
 * @author chengzhengzheng
 * @date 2021/9/18
 */
@SuppressWarnings("all")
public class RedisKeysConstant {
    public static final String STOCK_COUNT   = "sock_count";
    public static final String STOCK_SALE    = "stock_sale";
    public static final String STOCK_VERSION = "stock_version";
    public static final String STOCK_ID      = "stock_id";
    public static final String STOCK_NAME      = "stock_name";

    public static String allSeckillInfo() {
        return "seck:all:list";
    }

    public static String getSeckillInfo(String id) {
        return String.format("seck:%s:info", id);
    }

    public static String getSeckillInfoLock(long seckillId) {
        return null;
    }
}
