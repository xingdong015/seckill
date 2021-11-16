package com.system.design.seckil.graphql.runtimeWiring.wiring;

import com.system.design.seckil.graphql.runtimeWiring.component.AbstractRuntimeWiring;
import com.system.design.seckil.graphql.runtimeWiring.service.ProductImpl;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/12 4:47 下午
 * @description：
 * @modified By：`
 * @version: 1.0
 */
@Component
public class ProductWiring extends AbstractRuntimeWiring<ProductImpl>  {

    @Override
    public String getMethodName() {
        return "product";
    }

    @Override
    public void loader(@Autowired RuntimeWiring.Builder builder) {
        super.loader(builder);
    }

}
