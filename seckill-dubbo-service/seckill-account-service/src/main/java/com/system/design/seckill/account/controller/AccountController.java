package com.system.design.seckill.account.controller;

import com.system.design.seckill.account.entity.MyResponse;
import com.system.design.seckill.account.service.AccountService;
import com.system.design.seckill.common.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-13 16:00
 */
@RestController
@RequestMapping("/account")
@CrossOrigin(allowCredentials="true")
@Slf4j
public class AccountController {
    @Resource
    private AccountService accountService;


    @GetMapping("/search/test")
    public MyResponse inputSuggest(){
        AccountDto accountDto = AccountDto.builder().accountId(1L).phone("18810889966").address("中关村").build();
        return MyResponse.success(accountService.selectByPage(accountDto));
    }






}
