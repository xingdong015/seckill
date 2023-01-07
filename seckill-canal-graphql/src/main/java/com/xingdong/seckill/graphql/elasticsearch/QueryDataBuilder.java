package com.xingdong.seckill.graphql.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * es 参数构建
 * @date 2021-11-16 18:02:30
 * @author 程征波
 */
public class QueryDataBuilder {

        private static JSONObject builderBaseQuery(List<JSONObject> mastQuery) {
            JSONObject baseQuery = new JSONObject();
            JSONObject mustQuery = new JSONObject();
            baseQuery.put("bool", mustQuery);
            mustQuery.put("must",mastQuery);
            return baseQuery;
        }

        public static QueryBuilder builder(String query) {
            if(StringUtils.isBlank(query)) {
                JSONObject jsonObject = JSONObject.parseObject("{bool: {must: [{match_all: {}}]}}");
                return QueryBuilders.wrapperQuery(jsonObject.toJSONString());
            }
            if(JSON.isValidObject(query)) {
                Map<String,Object> jsonMap = (Map<String,Object>)JSONObject.parseObject(query, Map.class);
                List<JSONObject> jsonObjectList = jsonMap.entrySet().stream().map(entry -> {
                    JSONObject mastQuery = new JSONObject();
                    JSONObject kvQuery = new JSONObject();
                    kvQuery.put(entry.getKey(), entry.getValue());
                    mastQuery.put("match",kvQuery);
                    return mastQuery;
                }).collect(Collectors.toList());
                JSONObject builderBaseQuery = builderBaseQuery(jsonObjectList);
                return QueryBuilders.wrapperQuery(builderBaseQuery.toJSONString());
            }
            return null;
        }
}
