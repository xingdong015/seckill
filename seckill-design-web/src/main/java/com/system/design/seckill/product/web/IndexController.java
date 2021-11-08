package com.system.design.seckill.product.web;

import com.system.design.seckill.common.api.IKillBuzService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/10/26
 */
@RestController
@RequestMapping("/v1")
public class IndexController {
    @Resource
    private IKillBuzService killBuzService;

}
