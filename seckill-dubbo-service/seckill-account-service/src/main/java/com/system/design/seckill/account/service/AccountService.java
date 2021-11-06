package com.system.design.seckill.account.service;

import com.system.design.seckill.common.api.IAccountService;
import com.system.design.seckill.common.entity.Account;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author chengzhengzheng
 * @date 2021/11/6
 */
@DubboService
public class AccountService implements IAccountService {
    @Override
    public Account findById(long id) {
        return null;
    }
}
