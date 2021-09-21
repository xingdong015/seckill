package com.system.design.seckill.web.controller;

import com.system.design.seckill.entity.SeckillInfo;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("v1")
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    public static void main(String[] args) {
        Mapper mapper = new DozerBeanMapper();
        SeckillInfo seckillInfo = new SeckillInfo();
        seckillInfo.setSeckillId(123L);
        seckillInfo.setCount(989L);
        Map    map    = mapper.map(seckillInfo, Map.class);
        System.out.println(map);
    }
}
