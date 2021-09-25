package com.system.design.seckill.common.security;

/**
 * @author chengzhengzheng
 * @date 2021/9/24
 */

public class ResponseWithSecurity<R> {
    private Boolean          pass;
    private SecurityCodeEnum code;
    private R                result;

    public ResponseWithSecurity() {
    }

    public ResponseWithSecurity(Boolean pass, SecurityCodeEnum code, R result) {
        this.pass   = pass;
        this.code   = code;
        this.result = result;
    }

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public SecurityCodeEnum getCode() {
        return code;
    }

    public void setCode(SecurityCodeEnum code) {
        this.code = code;
    }
}
