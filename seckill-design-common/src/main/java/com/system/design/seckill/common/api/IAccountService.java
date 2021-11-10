package com.system.design.seckill.common.api;

import com.system.design.seckill.common.po.Account;

/**
 * @author chengzhengzheng
 * @date 2021/11/6
 */
public interface IAccountService {
    Account findById(long id);
}
