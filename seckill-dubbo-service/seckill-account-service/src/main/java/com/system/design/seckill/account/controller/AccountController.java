package com.system.design.seckill.account.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.system.design.seckill.common.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

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


    /**
     *  @Description: 产品名称的条件输入框  自动补全，根据用户的输入联想到可能的词或者短语
     */
    @GetMapping("/search/{input}")
    public String inputSuggest(@PathVariable String input){
         return null;
    }






}
