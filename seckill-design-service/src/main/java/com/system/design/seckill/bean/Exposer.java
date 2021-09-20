package com.system.design.seckill.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@Data
@NoArgsConstructor
public class Exposer {
    //是否开启秒杀
    private boolean exposed;
    //加密措施
    private String md5;
    private long seckillId;
    //系统当前时间(毫秒)
    private long now;
    //秒杀的开启时间
    private long start;
    //秒杀的结束时间
    private long end;
}
