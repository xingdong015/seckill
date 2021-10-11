package com.system.design.seckill.productwarmservcie.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.design.seckill.productwarmservcie.entity.MyTest;
import com.system.design.seckill.productwarmservcie.mapper.MyTestMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-09-24 14:34
 */
@Api(value = "测试swagger")
@RestController
@RequestMapping("/myTest")
public class MyTestController {
    @Autowired
    private MyTestMapper myTestMapper;

    @ApiOperation("测试添加数据->自动填充时间和版本号,以及忽略属性字段")
    @PostMapping("/insert")
    public int testInsert(@RequestBody MyTest myTest){
        int insert = myTestMapper.insert(myTest);
        System.out.println(insert);
        return insert;
    }

    @ApiOperation("测试更新数据->自动填充更新时间")
    @PostMapping("/update")
    public int testupdata(@RequestBody MyTest myTest){
        LambdaQueryWrapper<MyTest> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(MyTest::getId,myTest.getId());
        int update = myTestMapper.update(myTest,lambdaQuery);
        System.out.println(update);
        return update;
    }

    @ApiOperation("测试更新->乐观锁需要先检索,在传入才能更新版本号")
    @PostMapping("/version")
    public int testupdata_version(@RequestBody MyTest myTest){
        MyTest myTest1 = myTestMapper.selectById(myTest.getId());
        myTest1.setAge(999);
        LambdaQueryWrapper<MyTest> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(MyTest::getId,myTest.getId());
        int update = myTestMapper.update(myTest1,lambdaQuery);
        System.out.println(update);
        return update;
    }

    @ApiOperation("测试分页检索")
    @GetMapping("/search/page/{currentPage}/{pageSize}")
    public Page testPage(@PathVariable Integer currentPage, @PathVariable Integer pageSize ){
        //1.创建分页对象
        Page<MyTest> myPage = new Page<>(currentPage, pageSize);
        //2.调用 mybatis-plus 提供的分页查询方法; .getRecords取出集合数据,不取的话会返回一些其它值
        LambdaQueryWrapper<MyTest> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(MyTest::getMyName,"ack").ge(MyTest::getAge,18);
        Page<MyTest> myTestPage = myTestMapper.selectPage(myPage, lambdaQuery);
        return myTestPage;
    }

}
