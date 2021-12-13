package com.system.design.seckill.web;

import com.system.design.seckill.common.api.IKillBuzService;
import com.system.design.seckill.common.dto.SeckillDto;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chengzhengzheng
 * @date 2021/10/26
 */
@RequestMapping("/v1")
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "index.jsp";
    }


}
