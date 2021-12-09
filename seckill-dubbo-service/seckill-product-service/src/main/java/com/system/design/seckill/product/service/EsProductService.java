package com.system.design.seckill.product.service;

import org.elasticsearch.action.search.SearchRequest;

import java.io.IOException;
import java.util.List;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-12-09 10:44
 */
public interface EsProductService {
    List doSearchRequest(SearchRequest searchRequest,  String suggestName) throws IOException;
}
