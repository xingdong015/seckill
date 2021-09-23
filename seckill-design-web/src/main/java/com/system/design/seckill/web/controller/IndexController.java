package com.system.design.seckill.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("v1")
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
