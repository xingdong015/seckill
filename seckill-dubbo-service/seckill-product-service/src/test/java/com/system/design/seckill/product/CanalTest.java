package com.system.design.seckill.product;

import com.system.design.seckill.SeckillProductServiceApplicationTests;
import com.system.design.seckill.product.canal.CanalClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-09 19:55
 */
@Slf4j
public class CanalTest extends SeckillProductServiceApplicationTests {
    @Resource
    private CanalClient canalClient;

    @Test
    void test(){
//        canalClient.run();
    }
}
