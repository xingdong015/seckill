package com.system.design.seckil.graphql.runtimeWiring.runtimeWiring;

import com.system.design.seckil.graphql.runtimeWiring.component.AbstractRuntimeWiring;
import com.system.design.seckil.graphql.runtimeWiring.response.ProductResponse;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/12 4:47 下午
 * @description：
 * @modified By：`
 * @version: 1.0
 */
@Component
public class ProductWiring extends AbstractRuntimeWiring<ProductResponse>  {

    public String getFieldName() {
        return "product";
    }

    public Product product(long id) {
        return new Product();
    }

    public Product productList(long id) {
        return new Product();
    }

}
