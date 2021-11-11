package com.system.design.seckill.product.canal.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.system.design.seckill.common.po.Product;
import com.system.design.seckill.product.es.ElasticEntity;
import com.system.design.seckill.product.es.EsOptionUtil;
import com.system.design.seckill.product.es.IndexNameConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-10 14:41
 */
@Slf4j
@Component
public class EsHandle {

    @Resource
    private EsOptionUtil esOptionUtil;

    /**
     * 数据处理
     *
     * @param entrys
     */
    @Async
    public void dataHandle(List<CanalEntry.Entry> entrys) throws InvalidProtocolBufferException {
        for (CanalEntry.Entry entry : entrys) {
            if (CanalEntry.EntryType.ROWDATA == entry.getEntryType()) {
                CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                esApiHandle(rowChange.getEventType(), entry.getHeader().getTableName(), rowChange.getRowDatasList());
            }
        }
    }

    private void esApiHandle(CanalEntry.EventType eventType, String tableName, List<CanalEntry.RowData> rowDatasList) {
        if (eventType == CanalEntry.EventType.DELETE) {
            List<ElasticEntity> elasticEntities = createColumnsObj(tableName, rowDatasList, "Before");
            //调用es接口进行删除
            esOptionUtil.deleteBatch(IndexNameConstant.T_PRODUCT, elasticEntities);
            log.info("### DELETE elasticEntities:{} ###", JSON.toJSONString(elasticEntities));
        } else if (eventType == CanalEntry.EventType.UPDATE) {
            List<ElasticEntity> elasticEntitiesBefore = createColumnsObj(tableName, rowDatasList, "Before");
            List<ElasticEntity> elasticEntitiesAfter = createColumnsObj(tableName, rowDatasList, "After");
            //调用es接口进行更新:先删除后添加
            esOptionUtil.deleteBatch(IndexNameConstant.T_PRODUCT, elasticEntitiesBefore);
            esOptionUtil.insertBatch(IndexNameConstant.T_PRODUCT, elasticEntitiesAfter);
            log.info("### UPDATE before:{} --- after:{} ###", JSON.toJSONString(elasticEntitiesBefore), JSON.toJSONString(elasticEntitiesAfter));
        } else if (eventType == CanalEntry.EventType.INSERT) {
            List<ElasticEntity> elasticEntities = createColumnsObj(tableName, rowDatasList, "After");
            //调用es接口进行保存
            esOptionUtil.insertBatch(IndexNameConstant.T_PRODUCT, elasticEntities);
            log.info("### INSERT elasticEntities:{} ###", JSON.toJSONString(elasticEntities));
        }
    }

    private List<ElasticEntity> createColumnsObj(String tableName, List<CanalEntry.RowData> rowDatasList, String flag) {
        List<ElasticEntity> elasticEntityList = new ArrayList<>();
        try {
            for (CanalEntry.RowData rowData : rowDatasList) {
                ElasticEntity<Object> elasticEntity = null;
                List<CanalEntry.Column> columnList = rowData.getAfterColumnsList();
                if ("Before".equals(flag)) {
                    columnList = rowData.getBeforeColumnsList();
                }
                if ("t_product".equals(tableName)) {
                    Product product = Product.builder().build();
                    createProduct(product, columnList);
                    Map map = JSONObject.parseObject(JSONObject.toJSONString(product), Map.class);
                    elasticEntity = ElasticEntity.builder().id(product.getId().toString()).data(map).build();
                }
                elasticEntityList.add(elasticEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elasticEntityList;
    }


    private static void createProduct(Product product, List<CanalEntry.Column> columnList) {
        columnList.stream().forEach(column -> {
            switch (column.getName()) {
                case "id":
                    product.setId(Long.valueOf(column.getValue()));
                    break;
                case "product_name":
                    product.setProductName(column.getValue());
                    break;
                case "product_desc":
                    product.setProductDesc(column.getValue());
                    break;
                case "price":
                    product.setPrice(new BigDecimal(column.getValue()));
                    break;
                case "create_time":
                    product.setCreateTime(Long.valueOf(column.getValue()));
                    break;
                case "update_time":
                    product.setUpdateTime(Long.valueOf(column.getValue()));
                    break;
                default:
                    log.error("### 未匹配到字段name:{}, value:{} ###", column.getName(), column.getValue());
                    break;
            }
        });
    }
}
