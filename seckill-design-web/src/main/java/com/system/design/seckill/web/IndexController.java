package com.system.design.seckill.web;

import com.system.design.seckill.common.api.IAccountService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chengzhengzheng
 * @date 2021/10/26
 */
@Controller
public class IndexController {

//    @DubboReference(version = "1.0.0")
//    private IKillBuzService killBuzService;

    @DubboReference
    private IAccountService accountService;

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        //1. 下发所有活动中的 product
        //2. 端上根据倒计时开始执行秒杀活动
        //3.
//        return JSON.toJSONString(killBuzService.getSeckillList());
        return "";
    }


}
