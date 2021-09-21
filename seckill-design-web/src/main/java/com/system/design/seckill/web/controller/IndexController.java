package com.system.design.seckill.web.controller;

<<<<<<< HEAD
=======
import com.system.design.seckill.entity.Seckill;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
>>>>>>> dd0a77394194235c40290da3abec7234dd500561
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1")
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
<<<<<<< HEAD
=======

    public static void main(String[] args) {
        Mapper mapper = new DozerBeanMapper();
        Seckill seckillInfo = new Seckill();
        seckillInfo.setSeckillId(123L);
        seckillInfo.setCount(989L);
        Map    map    = mapper.map(seckillInfo, Map.class);
        System.out.println(map);
    }
>>>>>>> dd0a77394194235c40290da3abec7234dd500561
}
