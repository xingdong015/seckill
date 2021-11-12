package com.system.design.seckill.product.canal.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.system.design.seckill.product.es.ElasticEntity;
import com.system.design.seckill.product.es.EsOptionUtil;
import com.system.design.seckill.product.es.IndexNameConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        for(CanalEntry.Entry entry : entrys) {
            if(CanalEntry.EntryType.ROWDATA == entry.getEntryType() && entry.getHeader().getTableName().startsWith("t_")) {
                CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                esApiHandle(entry, rowChange);
            }
        }
    }

    private void esApiHandle(CanalEntry.Entry entry, CanalEntry.RowChange rowChange) {
        CanalEntry.EventType eventType = rowChange.getEventType();
        String tableName = entry.getHeader().getTableName();
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        // ddl
        if(rowChange.getIsDdl()) {
            //自动创建index
            if(eventType == CanalEntry.EventType.CREATE) {
                String idxMapper = createColumnsMapper(rowChange);
                esOptionUtil.createIndex(tableName, idxMapper);
            }
        } else {
            // dml
            if(eventType == CanalEntry.EventType.DELETE) {
                List<ElasticEntity> elasticEntities = createColumnsObj(rowDatasList, "Before");
                //调用es接口进行删除
                esOptionUtil.deleteBatch(IndexNameConstant.getIndexName(tableName), elasticEntities.stream().map(ElasticEntity::getId).collect(Collectors.toList()));
                log.info("### DELETE elasticEntities:{} ###", JSON.toJSONString(elasticEntities));
            } else if(eventType == CanalEntry.EventType.UPDATE) {
                List<ElasticEntity> elasticEntitiesBefore = createColumnsObj(rowDatasList, "Before");
                List<ElasticEntity> elasticEntitiesAfter = createColumnsObj(rowDatasList, "After");
                //调用es接口进行更新:先删除后添加
                esOptionUtil.deleteBatch(IndexNameConstant.getIndexName(tableName), elasticEntitiesBefore.stream().map(ElasticEntity::getId).collect(Collectors.toList()));
                esOptionUtil.insertBatch(IndexNameConstant.getIndexName(tableName), elasticEntitiesAfter);
                log.info("### UPDATE before:{} --- after:{} ###", JSON.toJSONString(elasticEntitiesBefore), JSON.toJSONString(elasticEntitiesAfter));
            } else if(eventType == CanalEntry.EventType.INSERT) {
                List<ElasticEntity> elasticEntities = createColumnsObj(rowDatasList, "After");
                //调用es接口进行保存
                esOptionUtil.insertBatch(IndexNameConstant.getIndexName(tableName), elasticEntities);
                log.info("### INSERT elasticEntities:{} ###", JSON.toJSONString(elasticEntities));
            }
        }
    }

    private String createColumnsMapper(CanalEntry.RowChange rowChange) {
        String sql = rowChange.getSql();
        log.info("{} ddl => {}", System.currentTimeMillis(), sql);
        return null;
    }

    private List<ElasticEntity> createColumnsObj(List<CanalEntry.RowData> rowDatasList, String flag) {
        List<ElasticEntity> elasticEntityList = new ArrayList<>();
        try {
            for(CanalEntry.RowData rowData : rowDatasList) {
                ElasticEntity elasticEntity;
                List<CanalEntry.Column> columnList = rowData.getAfterColumnsList();
                if("Before".equals(flag)) {
                    columnList = rowData.getBeforeColumnsList();
                }
//                if ("t_product".equals(tableName)) {
//                    Product product = Product.builder().build();
//                    createProduct(product, columnList);
//                    Map map = JSONObject.parseObject(JSONObject.toJSONString(product), Map.class);
//                    elasticEntity = ElasticEntity.builder().id(product.getId().toString()).data(map).build();
//                }

                Map<Object, Object> objMap = columnList.stream().collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));
                elasticEntity = ElasticEntity.builder().id(objMap.get("id").toString()).data(objMap).build();
                elasticEntityList.add(elasticEntity);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return elasticEntityList;
    }


//    private static void createProduct(Product product, List<CanalEntry.Column> columnList) {
//        columnList.stream().forEach(column -> {
//            switch(column.getName()) {
//                case "id":
//                    product.setId(Long.valueOf(column.getValue()));
//                    break;
//                case "product_name":
//                    product.setProductName(column.getValue());
//                    break;
//                case "product_desc":
//                    product.setProductDesc(column.getValue());
//                    break;
//                case "price":
//                    product.setPrice(new BigDecimal(column.getValue()));
//                    break;
//                case "create_time":
//                    product.setCreateTime(Long.valueOf(column.getValue()));
//                    break;
//                case "update_time":
//                    product.setUpdateTime(Long.valueOf(column.getValue()));
//                    break;
//                default:
//                    log.error("### 未匹配到字段name:{}, value:{} ###", column.getName(), column.getValue());
//                    break;
//            }
//        });
//    }
}
