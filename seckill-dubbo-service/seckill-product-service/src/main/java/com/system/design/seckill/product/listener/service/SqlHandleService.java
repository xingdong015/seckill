package com.system.design.seckill.product.listener.service;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.system.design.seckill.product.listener.CanalDataHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 拼接canal监听到的sql
 * @author: 贾凯
 * @create: 2021-11-10 14:33
 */
@Slf4j
@Component(value = "sqlHandleService")
public class SqlHandleService implements CanalDataHandleStrategy {
    @Override
    public void CanalDataHandle(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (CanalEntry.EntryType.ROWDATA == entry.getEntryType()) {
                CanalEntry.RowChange rowChange = null;
                try {
                    rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                    CanalEntry.EventType eventType = rowChange.getEventType();
                    if (eventType == CanalEntry.EventType.DELETE) {
                        saveDeleteSql(entry);
                    } else if (eventType == CanalEntry.EventType.UPDATE) {
                        saveUpdateSql(entry);
                    } else if (eventType == CanalEntry.EventType.INSERT) {
                        saveInsertSql(entry);
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 保存更新语句
     *
     * @param entry
     */
    private static void saveUpdateSql(CanalEntry.Entry entry) {
        try {
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
            for (CanalEntry.RowData rowData : rowDatasList) {
                List<CanalEntry.Column> newColumnList = rowData.getAfterColumnsList();
                StringBuffer sql = new StringBuffer("update " +
                        entry.getHeader().getTableName() + " set ");
                for (int i = 0; i < newColumnList.size(); i++) {
                    sql.append(" " + newColumnList.get(i).getName()
                            + " = '" + newColumnList.get(i).getValue() + "'");
                    if (i != newColumnList.size() - 1) {
                        sql.append(",");
                    }
                }
                sql.append(" where ");
                List<CanalEntry.Column> oldColumnList = rowData.getBeforeColumnsList();
                for (CanalEntry.Column column : oldColumnList) {
                    if (column.getIsKey()) {
                        //暂时只支持单一主键
                        sql.append(column.getName() + "=" + column.getValue());
                        break;
                    }
                }
                log.info("### sql: {} ###",sql.toString());
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
    /**
     * 保存删除语句
     *
     * @param entry
     */
    private static void saveDeleteSql(CanalEntry.Entry entry) {
        try {
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
            for (CanalEntry.RowData rowData : rowDatasList) {
                List<CanalEntry.Column> columnList = rowData.getBeforeColumnsList();
                StringBuffer sql = new StringBuffer("delete from " +
                        entry.getHeader().getTableName() + " where ");
                for (CanalEntry.Column column : columnList) {
                    if (column.getIsKey()) {
                        //暂时只支持单一主键
                        sql.append(column.getName() + "=" + column.getValue());
                        break;
                    }
                }
                log.info("### sql: {} ###",sql.toString());
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
    /**
     * 保存插入语句
     *
     * @param entry
     */
    private static void saveInsertSql(CanalEntry.Entry entry) {
        try {
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
            for (CanalEntry.RowData rowData : rowDatasList) {
                List<CanalEntry.Column> columnList = rowData.getAfterColumnsList();
                StringBuffer sql = new StringBuffer("insert into " +
                        entry.getHeader().getTableName() + " (");
                for (int i = 0; i < columnList.size(); i++) {
                    sql.append(columnList.get(i).getName());
                    if (i != columnList.size() - 1) {
                        sql.append(",");
                    }
                }
                sql.append(") VALUES (");
                for (int i = 0; i < columnList.size(); i++) {
                    sql.append("'" + columnList.get(i).getValue() + "'");
                    if (i != columnList.size() - 1) {
                        sql.append(",");
                    }
                }
                sql.append(")");
                log.info("### sql: {} ###",sql.toString());
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

}
