package com.system.design.seckil.graphql.runtimeWiring.dataLoad;

import com.system.design.seckil.graphql.runtimeWiring.component.CustomizerRuntimeWiring;
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
public class ProductDataLoad implements CustomizerRuntimeWiring {

    @Override
    public void loader(RuntimeWiring.Builder builder) {

    }
}
