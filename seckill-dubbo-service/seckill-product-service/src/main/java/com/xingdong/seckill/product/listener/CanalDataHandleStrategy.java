package com.xingdong.seckill.product.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-18 18:59
 */
public interface CanalDataHandleStrategy {
    @Async
    void CanalDataHandle(List<CanalEntry.Entry> entrys);
}
