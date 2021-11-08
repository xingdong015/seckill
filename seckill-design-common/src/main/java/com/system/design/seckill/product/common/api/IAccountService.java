package com.system.design.seckill.product.common.api;

import com.system.design.seckill.product.common.entity.Account;

/**
 * @author chengzhengzheng
 * @date 2021/11/6
 */
public interface IAccountService {
    Account findById(long id);
}
