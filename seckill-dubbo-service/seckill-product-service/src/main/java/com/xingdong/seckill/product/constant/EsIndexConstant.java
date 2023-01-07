package com.xingdong.seckill.product.constant;

/**
 * @description: 索引名称常量
 * @author: 贾凯
 * @create: 2021-08-12 17:31
 */
public class EsIndexConstant {
    public static final String API_SKILL_DEV_ = "api_skill_dev-%s";

    public static String getIndexName(String tableName) {
            return String.format(API_SKILL_DEV_ , tableName);
    }

}
