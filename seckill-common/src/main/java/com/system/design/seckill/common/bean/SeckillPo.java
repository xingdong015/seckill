package com.system.design.seckill.common.bean;

import lombok.Data;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@Data
@SuppressWarnings("all")
public class SeckillPo {
    private long   id;
    private String name;
    private long    count;
    private long    sale;
    private String version;
}
