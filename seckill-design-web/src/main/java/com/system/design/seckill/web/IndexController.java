package com.system.design.seckill.web;

import com.system.design.seckill.common.api.IKillBuzService;
import com.system.design.seckill.common.dto.ProductDto;
import com.system.design.seckill.common.dto.SeckillDto;
import com.system.design.seckill.common.po.Seckill;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengzhengzheng
 * @date 2021/10/26
 */
@RestController
@RequestMapping("/v1")
public class IndexController {
    @DubboReference
    private IKillBuzService killBuzService;

    @RequestMapping("/add/kill")
    public void addKill(@RequestBody SeckillDto seckillDto) {
        killBuzService.addKill(seckillDto);
    }

}
