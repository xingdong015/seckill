package com.system.design.seckill.common.utils;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
public enum KillEventTopiEnum {
    KILL_SUCCESS("redis_kill_success", "redisKillSuccess", "redis秒杀成功通知"),
    PAY_STATUS_CHANGE("pay_status_change", "createOrder", "支付状态改变通知");
    private String topic;
    private String scene;
    private String desc;

    KillEventTopiEnum(String topic, String scene, String desc) {
        this.topic = topic;
        this.scene = scene;
        this.desc  = desc;
    }

    public String getTopic() {
        return topic;
    }
}
