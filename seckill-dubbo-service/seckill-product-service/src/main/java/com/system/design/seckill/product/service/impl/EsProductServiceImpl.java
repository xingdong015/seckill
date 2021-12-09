package com.system.design.seckill.product.service.impl;

import com.system.design.seckill.product.service.EsProductService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-12-09 10:45
 */
@Service
@Slf4j
public class EsProductServiceImpl implements EsProductService {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public List doSearchRequest(SearchRequest searchRequest,  String suggestName) throws IOException {
        //3.发送请求
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //4.处理相应
        List sugggestList = new ArrayList();
        if (RestStatus.OK.equals(searchResponse.status())){
            //获取建议结果
            Suggest suggest = searchResponse.getSuggest();
            TermSuggestion termSuggestion = suggest.getSuggestion(suggestName);
            termSuggestion.getEntries().stream().forEach(entry -> {
                log.info("前台输入---input: {}",entry.getText().toString());
                entry.getOptions().stream().forEach(option -> {
                    sugggestList.add(option.getText().toString());
                    log.info("词项建议---suggest: {}", option.getText().toString());
                });
            });
        }
        return sugggestList;
    }
}
