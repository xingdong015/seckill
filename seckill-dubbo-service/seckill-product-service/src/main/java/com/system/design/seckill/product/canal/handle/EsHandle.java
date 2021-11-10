package com.system.design.seckill.product.canal.handle;

import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
    public void dataHandle(List<CanalEntry.Entry> entrys) {

        log.info("数据更新到es");
    }
}
