package com.xingdong.seckill.product.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-08-09 12:13
 */
@Configuration
public class ESClientConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient() {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1", 9200, "http")
                )
        );
        return client;
    }

    /** 设置分片
     */
    public static void buildSetting(CreateIndexRequest request){

        request.settings(Settings.builder().put("index.number_of_shards",1)
                .put("index.number_of_replicas",1));
    }
}
