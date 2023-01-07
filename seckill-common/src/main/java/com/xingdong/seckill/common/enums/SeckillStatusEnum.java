package com.xingdong.seckill.common.enums;

public enum SeckillStatusEnum {

    SUCCESS(1001,"进入候选队列，秒杀结果等待后续通知"),
    END(1002,"秒杀结束"),
    REPEAT_KILL(1003,"重复秒杀"),
    INNER_ERROR(1004,"系统异常"),
    NO_STOCK(1005,"系统异常"),
    DATE_REWRITE(1006,"数据篡改");

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