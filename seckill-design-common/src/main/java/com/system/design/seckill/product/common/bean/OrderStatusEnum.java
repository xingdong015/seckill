package com.system.design.seckill.product.common.bean;

/**
 * @author chengzhengzheng
 * @date 2021/10/23
 */
public enum OrderStatusEnum {
    INIT(0, "未支付"),
    PAY_SUCCESS(1, "已支付"),
    PAY_FAIL(2, "支付失败"),
    FAIL(3, "废弃");
    private Integer    status;
    private String desc;

    OrderStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc   = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
