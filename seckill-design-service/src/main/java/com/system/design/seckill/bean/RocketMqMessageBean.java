package com.system.design.seckill.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Data
@AllArgsConstructor
public class RocketMqMessageBean implements Serializable {
    private String  body;
    private Integer type;
    private long    currentTime;
}
