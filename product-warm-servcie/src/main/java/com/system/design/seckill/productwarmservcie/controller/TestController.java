package com.system.design.seckill.productwarmservcie.controller;

import com.system.design.seckill.productwarmservcie.entity.MyTest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-09-24 14:34
 */
@Api(value = "测试swagger")
@RestController
@RequestMapping("/test")
public class TestController {

    @ApiOperation("测试swagger请求")
    @GetMapping("/{name}")
    public MyTest test(@PathVariable String name){
        MyTest myTest = MyTest.builder().myName(name).myAge(18).build();
        return myTest;
    }
}
