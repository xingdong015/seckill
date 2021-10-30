package com.system.design.seckil.common.controller;

import com.system.design.seckil.common.service.KillBuzService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chengzhengzheng
 * @date 2021/10/30
 */
@RestController
@RequestMapping("/common")
public class IndexController {
    @Resource
    private KillBuzService killBuzService;

    @RequestMapping("/kill")
    @ResponseBody
    public Long kill(long killId, String userId){
        return killBuzService.doKill(killId,userId);
    }
}
