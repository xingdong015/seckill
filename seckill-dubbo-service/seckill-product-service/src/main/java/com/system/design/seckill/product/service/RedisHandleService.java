package com.system.design.seckill.product.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.system.design.seckill.common.po.Product;
import com.system.design.seckill.product.constant.EsIndexConstant;
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

    @Override
    public void CanalDataHandle(List<CanalEntry.Entry> entrys) {
        for(CanalEntry.Entry entry : entrys) {
            if(CanalEntry.EntryType.ROWDATA == entry.getEntryType() && entry.getHeader().getTableName().startsWith("t_")) {
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
    }


}
