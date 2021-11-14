package com.system.design.seckil.graphql.runtimeWiring.runtimeWiring;

import com.system.design.seckil.graphql.runtimeWiring.component.AbstractRuntimeWiring;
import com.system.design.seckil.graphql.runtimeWiring.response.ProductResponse;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.stereotype.Component;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/12 4:47 下午
 * @description：
 * @modified By：`
 * @version: 1.0
 */
@Component
public class ProductWiring extends AbstractRuntimeWiring<ProductResponse> {

    public String getFieldName() {
        return "t_product";
    }

    @Override
    public void loader(RuntimeWiring.Builder builder) {
        super.loader(builder);
    }
}
