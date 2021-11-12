package com.system.design.seckill.product.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.system.design.seckill.product.canal.handle.EsHandle;
import com.system.design.seckill.product.canal.handle.SqlHandle;
import com.system.design.seckill.product.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-11-09 18:52
 */
@Component
@Slf4j
public class CanalClient implements ApplicationRunner {
    @Value("${canal.host}")
    private String host;
    @Value("${canal.port}")
    private Integer port;
    @Value("${canal.destination}")
    private String destination;
    @Value("${canal.username}")
    private String username;
    @Value("${canal.password}")
    private String password;
    @Resource
    private SqlHandle sqlHandle;
    @Resource
    private EsHandle esHandle;
    @Resource
    private RedisUtils redisUtils;
    private static final int BATCH_SIZE = 1000;
    private static final int SLEEP_VALUE = 2000;
    private static final int EXPIRE_TIME = 20000;

    @Override
    public void run(ApplicationArguments args) {
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(host, port), destination, username, password);
        try {
            //打开连接
            connector.connect();
//            订阅数据库表,全部表
            connector.subscribe(".*\\..*");
//            connector.subscribe("seckill.t_product");
            //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
            connector.rollback();
            Jedis jedis = redisUtils.getJedis();
            String key = "redis:key";
            String randomString = UUID.randomUUID().toString();
            try {
                while (true) {
                    //获取分布式锁
                    boolean tryGetDistributedLock = redisUtils.tryGetDistributedLock(jedis,key, randomString, EXPIRE_TIME);
                    if (tryGetDistributedLock){
                        //尝试从master那边拉去数据batchSize条记录，有多少取多少
                        Message message = connector.getWithoutAck(BATCH_SIZE);
                        long batchId = message.getId();
                        int size = message.getEntries().size();
                        if (batchId == -1 || size == 0) {
                            Thread.sleep(SLEEP_VALUE);
                        } else {
                            //异步调用处理,拼接sql
//                        sqlHandle.dataHandle(message.getEntries());
                            //异步调用处理,组装成操作标识和es操作对象，调用es接口
                            esHandle.dataHandle(message.getEntries());
                        }
                        connector.ack(batchId);
                    }else {
                        Thread.sleep(SLEEP_VALUE);
                    }
                }
            } catch (InterruptedException | InvalidProtocolBufferException e) {
                e.printStackTrace();
            }finally {
                //异常时，释放redis分布式锁
                redisUtils.releaseDistributedLock(jedis,key, randomString);
            }
        } finally {
            connector.disconnect();
        }
    }
}
