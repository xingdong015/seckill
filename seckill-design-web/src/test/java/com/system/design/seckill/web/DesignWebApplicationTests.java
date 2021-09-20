package com.system.design.seckill.web;

import com.system.design.seckill.entity.SeckillInfo;
import com.system.design.seckill.service.SeckillServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DesignWebApplicationTests {

    @Autowired
    private SeckillServiceImpl seckillService;

    @Test
    public void test() {
        SeckillInfo seckillInfo = new SeckillInfo();
        seckillInfo.setCount(9999L);
        seckillInfo.setSeckillId(3L);
        seckillService.updateById(seckillInfo);
    }

    @Test
    public void save() {
        SeckillInfo seckillInfo = new SeckillInfo();
        seckillInfo.setCount(888L);
        seckillInfo.setSeckillId(3L);
        seckillService.save(seckillInfo);
    }

}
