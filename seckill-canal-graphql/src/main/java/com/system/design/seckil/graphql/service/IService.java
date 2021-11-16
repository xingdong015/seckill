package com.system.design.seckil.graphql.service;

import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 程征波
 * @date 2021/11/13
 */
public interface IService {

    Object findOne(DataFetchingEnvironment env);

    List findList(DataFetchingEnvironment env);

    Page findPage(DataFetchingEnvironment env);
}
