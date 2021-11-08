package com.system.design.seckill.product.common.bean;

import java.io.Serializable;

/**
 * @author chengzhengzheng
 * @date 2021/11/6
 */
public class Resp<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String  desc;
    private T       data;

    public Resp<T> success(T data) {
        return code(ApiErrorCodeEnum.SUCCESS.getCode()).desc(ApiErrorCodeEnum.SUCCESS.getDesc()).data(data);
    }

    public Resp<T> failed(ApiErrorCodeEnum apiErrorCodeEnum) {
        return code(apiErrorCodeEnum.getCode()).desc(apiErrorCodeEnum.getDesc());
    }

    public Resp<T> failed(Integer code,String message){
        return code(code).desc(message);
    }

    public Resp<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public Resp<T> desc(String desc) {
        this.desc = desc;
        return this;
    }

    public Resp<T> data(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public T getData() {
        return data;
    }
}
