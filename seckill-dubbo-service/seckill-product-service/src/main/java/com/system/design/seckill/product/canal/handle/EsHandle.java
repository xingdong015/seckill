package com.system.design.seckill.product.canal.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.system.design.seckill.common.po.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-10 14:41
 */
@Slf4j
@Component
public class EsHandle {

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
                CanalEntry.EventType eventType = rowChange.getEventType();
                List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
                if ("t_product".equals(entry.getHeader().getTableName())) {
                    esApiHandle(eventType, rowDatasList);
                }
            }
        }
    }

    private void esApiHandle(CanalEntry.EventType eventType, List<CanalEntry.RowData> rowDatasList) {
        if (eventType == CanalEntry.EventType.DELETE) {
            List<Product> productList = createBeforeColumnsObj(rowDatasList);
            log.info("### DELETE productList:{} ###", JSON.toJSONString(productList));
            //调用es接口进行删除

        } else if (eventType == CanalEntry.EventType.UPDATE) {
            List<Product> productList = createAfterColumnsObj(rowDatasList);
            log.info("### UPDATE productList:{} ###", JSON.toJSONString(productList));
            //调用es接口进行更新

        } else if (eventType == CanalEntry.EventType.INSERT) {
            List<Product> productList = createAfterColumnsObj(rowDatasList);
            log.info("### INSERT productList:{} ###", JSON.toJSONString(productList));
            //调用es接口进行保存

        }
    }

    private List<Product> createAfterColumnsObj(List<CanalEntry.RowData> rowDatasList) {
        List<Product> productList = new ArrayList<>();
        try {
            for (CanalEntry.RowData rowData : rowDatasList) {
                List<CanalEntry.Column> columnList = rowData.getAfterColumnsList();
                Product product = Product.builder().build();
                createProduct(product, columnList);
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    private List<Product> createBeforeColumnsObj(List<CanalEntry.RowData> rowDatasList) {
        List<Product> productList = new ArrayList<>();
        try {
            for (CanalEntry.RowData rowData : rowDatasList) {
                List<CanalEntry.Column> columnList = rowData.getBeforeColumnsList();
                Product product = Product.builder().build();
                createProduct(product, columnList);
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
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
