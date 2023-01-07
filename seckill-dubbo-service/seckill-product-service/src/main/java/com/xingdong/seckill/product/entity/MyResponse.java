package com.xingdong.seckill.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-13 16:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyResponse {
    private Integer code;
    private String msg;
    private Object data;

    public static MyResponse success(){
        return MyResponse.builder().code(200).msg("success").data(null).build();
    }

    public static MyResponse success(Object data){
        return MyResponse.builder().code(200).msg("success").data(data).build();
    }

    public static MyResponse error(){
        return MyResponse.builder().code(500).msg("error").data(null).build();
    }

    public static MyResponse error(Object data){
        return MyResponse.builder().code(500).msg("error").data(data).build();
    }

}
