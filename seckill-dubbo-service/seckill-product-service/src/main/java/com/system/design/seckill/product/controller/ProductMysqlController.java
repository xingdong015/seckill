package com.system.design.seckill.product.controller;

import com.system.design.seckill.product.entity.MyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 进行redis缓存和mysql检索入口
 * @author: 贾凯
 * @create: 2021-11-13 16:22
 */
@RestController
@RequestMapping("/mysql/product")
@CrossOrigin(allowCredentials="true")
@Slf4j
public class ProductMysqlController {

    @PostMapping("/search")
    public MyResponse search(String body){
        //1.redis中检索

        //2.redis不存在，去mysql检索
        return MyResponse.success();
    }

    @PostMapping("/update")
    public MyResponse update(String body){
        //缓存一致性处理。。。
        //1.更新数据库

        //2.删除缓存
        return MyResponse.success();
    }

    @PostMapping("/save")
    public MyResponse save(String body){
        //直接存储到mysql，需要redis添加缓存？
        return MyResponse.success();
    }

    @PostMapping("/delete")
    public MyResponse delete(String body){
        //先删除缓存还是数据库？
        return MyResponse.success();
    }

}
