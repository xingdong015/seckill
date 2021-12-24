package com.system.design.seckill.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chengzhengzheng
 * @date 2021/10/26
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        //1. 下发所有活动中的 product
        //2. 端上根据倒计时开始执行秒杀活动
        //3.
        return "product";
    }


}
