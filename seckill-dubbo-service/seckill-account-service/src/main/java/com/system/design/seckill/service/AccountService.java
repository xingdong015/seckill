package com.system.design.seckill.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.design.seckill.mapper.AccountMapper;
import com.system.design.seckill.common.api.IAccountService;
import com.system.design.seckill.common.dto.AccountDto;
import com.system.design.seckill.common.po.Account;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-08 18:33
 */
@Service
public class AccountService implements IAccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Account findById(long id) {
        return accountMapper.selectById(id);
    }

    @Override
    public int createAccount(Account account) {
        return accountMapper.insert(account);
    }

    @Override
    public int deleteAccount(long accountId) {
        return accountMapper.deleteById(accountId);
    }

    @Override
    public int updateAccount(Account account) {
        return accountMapper.updateById(account);
    }

    @Override
    public List<Account> getAll() {
        return accountMapper.selectList(null);
    }

    @Override
    public IPage selectByPage(AccountDto accountDto) {
        Page<Account> objectPage = new Page<>();
        LambdaQueryWrapper<Account> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (accountDto != null){
            if (accountDto.getCurrentPage() != null){objectPage.setCurrent(accountDto.getCurrentPage());}
            if (accountDto.getPageSize() != null){objectPage.setSize(accountDto.getPageSize());}
            if (accountDto.getAccountId() != null){lambdaQueryWrapper.eq(Account::getAccountId, accountDto.getAccountId());}
            if (StringUtils.isNotBlank(accountDto.getEmail())){lambdaQueryWrapper.eq(Account::getEmail, accountDto.getEmail());}
            if (StringUtils.isNotBlank(accountDto.getPhone())){lambdaQueryWrapper.eq(Account::getPhone, accountDto.getPhone());}
            if (StringUtils.isNotBlank(accountDto.getAddress())){lambdaQueryWrapper.like(Account::getAddress, accountDto.getAddress());}
        }

        return accountMapper.selectPage(objectPage, lambdaQueryWrapper);
    }
}
