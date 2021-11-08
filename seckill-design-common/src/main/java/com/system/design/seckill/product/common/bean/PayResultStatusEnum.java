package com.system.design.seckill.product.common.bean;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
public enum PayResultStatusEnum {

    SUCCESS(1,"支付成功"),
    FAIL(0,"支付失败"),
    REPEAT_KILL(-1,"订单不存在"),
    INNER_ERROR(-2,"系统异常");

    private int state;
    private String info;

    PayResultStatusEnum(int state, String info) {
        this.state = state;
        this.info  = info;
    }
}
