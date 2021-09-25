package com.system.design.seckill.productwarmservcie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-09-24 14:51
 */
@ApiModel("测试对象")
@TableName("t_my_test")
@Data@Builder@AllArgsConstructor
public class MyTest {
    @ApiModelProperty(value = "测试姓名",name = "username",example = "jack")
    private String myName;
    @ApiModelProperty(value = "测试年龄",name = "age",example = "18")
    private Integer myAge;
}
