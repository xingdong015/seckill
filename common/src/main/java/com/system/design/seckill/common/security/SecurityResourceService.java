package com.system.design.seckill.common.security;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

/**
 * @author chengzhengzheng
 * @date 2021/9/24
 */
@Service
public class SecurityResourceService {

    public <R> ResponseWithSecurity check(SecurityResourceRequest resourceRequest, CallBack<R> callBack) {
        Entry entry = null;
        try {
            //1. 是否是黑名单用户
            //2. 是否来自于同一个ip
            //3. 是否来自于同一个用户
            //4. 限流
            entry = SphU.entry("resourceName");

            long killId = resourceRequest.getKillId();
            long userId = resourceRequest.getUserId();
            //ip 限流、用户Id限流  等等逻辑
            // 被保护的业务逻辑
            R result = callBack.execute();
            return new ResponseWithSecurity(true, SecurityCodeEnum.SUCCESS, result);
        } catch (BlockException ex) {
            ex.printStackTrace();
            return new ResponseWithSecurity(true, SecurityCodeEnum.FAILURE, "-1");
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }
}
