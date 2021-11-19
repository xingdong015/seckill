package com.system.design.seckill.product.listener.service;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.system.design.seckill.product.entity.ElasticEntity;
import com.system.design.seckill.product.listener.CanalDataHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 同步canal的监听数据到redis
 * @author: 贾凯
 * @create: 2021-11-18 18:28
 */
@Slf4j
@Component
public class RedisHandleService implements CanalDataHandleStrategy {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void CanalDataHandle(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (CanalEntry.EntryType.ROWDATA == entry.getEntryType() && entry.getHeader().getTableName().startsWith("t_")) {
                CanalEntry.RowChange rowChange = null;
                try {
                    rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                    redisHandle(entry, rowChange);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void redisHandle(CanalEntry.Entry entry, CanalEntry.RowChange rowChange) {
        CanalEntry.EventType eventType = rowChange.getEventType();
        String tableName = entry.getHeader().getTableName();
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        if (eventType == CanalEntry.EventType.DELETE) {
            List<Map<String, Object>> dataList = createColumnsObj(rowDatasList, "Before");
            //redis删除所有hash的key
            deleteKeysFromHash(dataList);
        } else if (eventType == CanalEntry.EventType.UPDATE) {
            List<Map<String, Object>> beforeDataList = createColumnsObj(rowDatasList, "Before");
            List<Map<String, Object>> afterDataList = createColumnsObj(rowDatasList, "After");
            //redis进行更新hash:先删除后添加
            deleteKeysFromHash(beforeDataList);
            saveMap2Hash(afterDataList);
        } else if (eventType == CanalEntry.EventType.INSERT) {
            List<Map<String, Object>> dataList = createColumnsObj(rowDatasList, "After");
            //redis进行hash保存所有的map
            saveMap2Hash(dataList);
        }
    }

    private void saveMap2Hash(List<Map<String, Object>> dataList) {
        dataList.stream().forEach(data -> {
            String key = CacheKey.getProductHash((String) data.get("id"));
            redisTemplate.opsForHash().putAll(key, data);
        });
    }

    private void deleteKeysFromHash(List<Map<String, Object>> dataList) {
        List<Object> idList = dataList.stream().map(map -> CacheKey.getProductHash((String) map.get("id"))).distinct().collect(Collectors.toList());
        redisTemplate.opsForHash().delete(idList);
    }


    private List<Map<String, Object>> createColumnsObj(List<CanalEntry.RowData> rowDatasList, String flag) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            for (CanalEntry.RowData rowData : rowDatasList) {
                ElasticEntity elasticEntity;
                List<CanalEntry.Column> columnList = rowData.getAfterColumnsList();
                if ("Before".equals(flag)) {
                    columnList = rowData.getBeforeColumnsList();
                }
                Map<String, Object> objMap = columnList.stream().collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));
                mapList.add(objMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
    }

}
