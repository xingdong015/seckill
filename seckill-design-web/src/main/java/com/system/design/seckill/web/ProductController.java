package com.system.design.seckill.web;

import com.alibaba.fastjson.JSON;
import com.system.design.seckill.common.api.IAccountService;
import com.system.design.seckill.common.api.IKillBuzService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author chengzhengzheng
 * @date 2021/10/26
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    /**
     * 这里如果不添加这个 version=1.0.0 会导致注入失败问题、排查了好久、
     * 源码翻了好几遍。😞😞😞😞😞😞😞😞😞😞😞😞😞😞😞
     */
    @DubboReference(version = "1.0.0")
    private IAccountService accountService;

    @RequestMapping("/list")
    public String list(Model model) {
        final List<Object> goods = new ArrayList<>();
        model.addAttribute("goods", goods);
        return "product_list";
    }

    @RequestMapping("/detail")
    public String detail(@RequestParam("id") String id) {
        return "";
    }

}
