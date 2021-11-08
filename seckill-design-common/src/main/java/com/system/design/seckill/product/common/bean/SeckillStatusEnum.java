package com.system.design.seckill.product.common.bean;

public enum SeckillStatusEnum {

    SUCCESS(1,"进入候选队列，秒杀结果等待后续通知"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATE_REWRITE(-3,"数据篡改");

    private int state;
    private String info;

    SeckillStatusEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }


    public String getInfo() {
        return info;
    }


    public static SeckillStatusEnum stateOf(int index)
    {
        for (SeckillStatusEnum state : values())
        {
            if (state.getState()==index)
            {
                return state;
            }
        }
        return null;
    }
}