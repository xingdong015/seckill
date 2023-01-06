package com.system.design.seckill.common.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.system.design.seckill.common.dto.AccountDto;
import com.system.design.seckill.common.po.Account;

import java.util.List;

/**
 * @author chengzhengzheng
 * @date 2021/11/6
 */
public interface IAccountService {
    Account findById(long id);
    int createAccount(Account account);
    int deleteAccount(long accountId);
    int updateAccount(Account account);
    List<Account> getAll();
}
