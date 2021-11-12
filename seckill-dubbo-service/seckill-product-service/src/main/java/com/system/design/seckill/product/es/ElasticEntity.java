package com.system.design.seckill.product.es;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-03-17 14:23
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElasticEntity {

    /**
     * 主键标识，用户ES持久化
     */
    private String id;

    /**
     * JSON对象，实际存储数据
     */
    private Map<Object,Object> data;
}

