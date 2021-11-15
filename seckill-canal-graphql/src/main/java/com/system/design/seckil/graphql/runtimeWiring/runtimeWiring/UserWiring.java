package com.system.design.seckil.graphql.runtimeWiring.runtimeWiring;

import com.system.design.seckil.graphql.runtimeWiring.component.AbstractRuntimeWiring;
import com.system.design.seckil.graphql.runtimeWiring.response.UserResponse;
import org.springframework.stereotype.Component;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/12 4:47 下午
 * @description：
 * @modified By：`
 * @version: 1.0
 */
@Component
public class UserWiring extends AbstractRuntimeWiring<UserResponse> {

    @Override
    public String getMethodName() {
        return "user";
    }

}
