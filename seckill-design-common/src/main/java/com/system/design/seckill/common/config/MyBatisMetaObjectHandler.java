package com.system.design.seckill.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-08 20:22
 */
@Component
public class MyBatisMetaObjectHandler implements MetaObjectHandler{
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "create_time", Long.class, new Date().getTime());
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "update_time", Long.class, new Date().getTime());
    }
}
