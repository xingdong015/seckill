package com.system.design.seckill.common.security;

/**
 * @author chengzhengzheng
 * @date 2021/9/24
 */
public class SecurityResourceRequest {
    private long   userId;
    private String ip;
    private long   killId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getKillId() {
        return killId;
    }

    public void setKillId(long killId) {
        this.killId = killId;
    }
}
