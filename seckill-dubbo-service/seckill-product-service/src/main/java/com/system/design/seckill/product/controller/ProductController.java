package com.system.design.seckill.product.controller;

import com.system.design.seckill.product.entity.MyResponse;
import com.system.design.seckill.product.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: 进行es检索入口
 * @author: 贾凯
 * @create: 2021-11-13 16:00
 */
@RestController
@RequestMapping("/es/product")
@CrossOrigin(allowCredentials="true")
@Slf4j
public class ProductController {
    @Resource
    private EsUtils esUtils;

    @PostMapping("/save")
    public MyResponse save(String body){
        //1.判断是否有索引名称，没有则创建索引

        //2.保存
        return MyResponse.success();
    }

    @PostMapping("/saveBatch")
    public MyResponse saveBatch(String body){
        //1.判断是否有索引名称，没有则创建索引

        //2.保存
        return MyResponse.success();
    }

    @PostMapping("/delete")
    public MyResponse delete(String body){
        //1.判断是否有索引名称，有则删除

        //2.没有则抛出信息
        return MyResponse.success();
    }

    @PostMapping("/deleteBatch")
    public MyResponse deleteBatch(String body){
        //1.判断是否有索引名称，有则删除

        //2.没有则抛出信息
        return MyResponse.success();
    }

    @PostMapping("/search")
    public MyResponse search(String body){
        //1.判断是否有索引名称，有则检索

        //2.分页条件检索，加聚合，加关键词渲染
        return MyResponse.success();
    }

    @PostMapping("/search/all")
    public MyResponse searchAll(){
        //查询所有
        return MyResponse.success();
    }



}
