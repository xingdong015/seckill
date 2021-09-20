package com.system.design.seckill.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.design.seckill.bean.SeckillPo;
import com.system.design.seckill.bean.SeckillResultStatus;
import com.system.design.seckill.service.SeckillBuzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillBuzService seckillBuzService;
    @Autowired
    private ObjectMapper      mapper;

    @GetMapping(value = "/list/all")
    public List<SeckillPo> getSeckillList() throws JsonProcessingException {
        return seckillBuzService.getSeckillList();
    }

    @GetMapping(value = "/list")
    public SeckillPo getSeckillById(@RequestParam String id) throws JsonProcessingException {
        return seckillBuzService.getById(id);
    }

    @GetMapping(value = "/execute")
    public SeckillResultStatus executeSeckill(@RequestParam long seckillId, @RequestParam long phone) {
        return seckillBuzService.executeSeckill(seckillId, phone);
    }
}
