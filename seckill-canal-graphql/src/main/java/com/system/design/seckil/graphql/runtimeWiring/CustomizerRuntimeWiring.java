package com.system.design.seckil.graphql.runtimeWiring;


import graphql.schema.idl.RuntimeWiring;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/12 4:35 下午
 * @description：自定义包装器顶层接口
 * @modified By：`
 * @version: 1.0
 */
@FunctionalInterface
public interface CustomizerRuntimeWiring {

    /**
     * 基础数据加载
     *
     * @param builder
     */
    void loader(RuntimeWiring.Builder builder);

}
