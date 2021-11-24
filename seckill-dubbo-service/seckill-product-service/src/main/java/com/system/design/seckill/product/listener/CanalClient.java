package com.system.design.seckill.product.listener;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.system.design.seckill.product.utils.RedisUtils;
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
 * @description:canal应该就是canalserver端复制几份example取不同名字，instance.properties文件修改
 * canal.mq.topic=example；  然后客户端创建连接的时候，连接不同example就让redis和es分开处理了。canal是将mysql变化的数据缓存在每个example下面的队列了
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
    private CanalDataHandleFactory canalDataHandleFactory;
    @Resource
    private RedisUtils redisUtils;
    private static final int BATCH_SIZE = 1000;
    private static final int SLEEP_VALUE = 2000;
    private static final int EXPIRE_TIME = 20000;

    @Override
    public void run(ApplicationArguments args) {
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(host, port), destination, username, password);
        try {
            //打开连接
            connector.connect();
            //订阅数据库表,全部表
            connector.subscribe(".*\\..*");
            //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
            connector.rollback();
            while (true) {
                //尝试从master那边拉去数据batchSize条记录，有多少取多少
                Message message = connector.getWithoutAck(BATCH_SIZE);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    Thread.sleep(SLEEP_VALUE);
                } else {
                    Jedis jedis = redisUtils.getJedis();
                    String key = "key:batchId:" + batchId;
                    String value = UUID.randomUUID().toString();
                    try {
                        boolean tryGetDistributedLock = redisUtils.tryGetDistributedLock(jedis, key, value, EXPIRE_TIME);
                        if (tryGetDistributedLock) {
                            //工厂加策略模式，调用异步方法处理
//                            canalDataHandleFactory.getCanalDataHandleStrategy("esHandleService").CanalDataHandle(message.getEntries());
                            canalDataHandleFactory.getCanalDataHandleStrategy("redisHandleService").CanalDataHandle(message.getEntries());
//                            canalDataHandleFactory.getCanalDataHandleStrategy("sqlHandleService").CanalDataHandle(message.getEntries());
                            connector.ack(batchId);
                        } else {
                            Thread.sleep(SLEEP_VALUE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        redisUtils.releaseDistributedLock(jedis, key, value);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }
    }
}