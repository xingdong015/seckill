package com.system.design.seckill.dbservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.design.seckill.entity.Account;
import com.system.design.seckill.mapper.AccountInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
public class AccountService extends ServiceImpl<AccountInfoMapper, Account> {
}
