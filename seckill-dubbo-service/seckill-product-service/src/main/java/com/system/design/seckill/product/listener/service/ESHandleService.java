package com.system.design.seckill.product.listener.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.system.design.seckill.product.constant.EsIndexConstant;
import com.system.design.seckill.product.entity.ElasticEntity;
import com.system.design.seckill.product.listener.CanalDataHandleStrategy;
import com.system.design.seckill.product.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
@Component(value = "esHandleService")
public class ESHandleService implements CanalDataHandleStrategy {

    @Resource
    private EsUtils esUtils;

    @Override
    public void CanalDataHandle(List<CanalEntry.Entry> entrys) {
        for(CanalEntry.Entry entry : entrys) {
            if(CanalEntry.EntryType.ROWDATA == entry.getEntryType() && entry.getHeader().getTableName().startsWith("t_")) {
                CanalEntry.RowChange rowChange = null;
                try {
                    rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                    esApiHandle(entry, rowChange);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
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
                esUtils.createIndex(tableName, idxMapper);
            }
        } else {
            // dml
            List<ElasticEntity> elasticEntities = null;
            if(eventType == CanalEntry.EventType.DELETE) {
                elasticEntities = createColumnsObj(rowDatasList, "Before");
                //调用es接口进行删除
                esUtils.deleteBatch(EsIndexConstant.getIndexName(tableName), elasticEntities.stream().map(ElasticEntity::getId).collect(Collectors.toList()));
            } else if(eventType == CanalEntry.EventType.UPDATE) {
                List<ElasticEntity> elasticEntitiesBefore = createColumnsObj(rowDatasList, "Before");
                elasticEntities = createColumnsObj(rowDatasList, "After");
                //调用es接口进行更新:先删除后添加
                esUtils.deleteBatch(EsIndexConstant.getIndexName(tableName), elasticEntitiesBefore.stream().map(ElasticEntity::getId).collect(Collectors.toList()));
                esUtils.insertBatch(EsIndexConstant.getIndexName(tableName), elasticEntities);
            } else if(eventType == CanalEntry.EventType.INSERT) {
                elasticEntities = createColumnsObj(rowDatasList, "After");
                //调用es接口进行保存
                esUtils.insertBatch(EsIndexConstant.getIndexName(tableName), elasticEntities);
            }
            log.info("### {}, elasticEntities:{} ###", eventType, JSON.toJSONString(elasticEntities));
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
                Map<Object, Object> objMap = columnList.stream().collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));
                elasticEntity = ElasticEntity.builder().id(objMap.get("id").toString()).data(objMap).build();
                elasticEntityList.add(elasticEntity);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return elasticEntityList;
    }

}
