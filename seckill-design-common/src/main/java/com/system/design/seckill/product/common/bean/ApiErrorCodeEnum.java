package com.system.design.seckill.product.common.bean;

/**
 * @author chengzhengzheng
 * @date 2021/11/6
 */
public enum ApiErrorCodeEnum {
    SUCCESS(0, "success"),
    FAIL(1, "failed"),


    PARAMS_ERROR(101, "参数错误"),
    ;

    private Integer code;
    private String  desc;

    ApiErrorCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }

}
