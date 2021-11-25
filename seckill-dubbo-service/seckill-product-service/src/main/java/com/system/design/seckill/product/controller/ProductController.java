package com.system.design.seckill.product.controller;

import com.system.design.seckill.common.dto.ProductDto;
import com.system.design.seckill.product.constant.EsIndexConstant;
import com.system.design.seckill.product.entity.MyResponse;
import com.system.design.seckill.product.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @description: 进行es检索入口
 * @author: 贾凯
 * @create: 2021-11-13 16:00
 */
@RestController
@RequestMapping("/es/product")
@CrossOrigin(allowCredentials="true")
@Slf4j
public class ProductController {
    @Resource
    private EsUtils esUtils;


    @PostMapping("/search")
    public MyResponse search(@RequestBody ProductDto productDto){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (productDto != null){
            if (StringUtils.isNotEmpty(productDto.getProductName())){
                boolQuery.must(QueryBuilders.matchQuery("product_name",productDto.getProductName()));
            }
            if (StringUtils.isNotEmpty(productDto.getProductDesc())){
                boolQuery.must(QueryBuilders.matchQuery("product_desc",productDto.getProductDesc()));
            }
            if (productDto.getMinPrice() != null){
                boolQuery.must(QueryBuilders.rangeQuery("price").gte(productDto.getMinPrice()));
            }
            if (productDto.getMaxPrice() != null){
                boolQuery.must(QueryBuilders.rangeQuery("price").lte(productDto.getMaxPrice()));
            }
        }


        Optional<Object> optional = null;
        try {
            optional = esUtils.searchSimple(EsIndexConstant.getIndexName("t_product"), boolQuery, productDto.getCurrentPage().intValue(), productDto.getPageSize().intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2.分页条件检索，加聚合，加关键词渲染
        return MyResponse.success(optional);
    }




}
