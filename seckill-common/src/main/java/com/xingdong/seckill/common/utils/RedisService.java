//package com.xingdong.seckill.common.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
//@Slf4j
//@Service
//public class RedisService {
//    @Autowired
//    private              StringRedisTemplate stringRedisTemplate;
//    private static final Long                TIME_OUT = 300000L;
//
//    /**
//     * 根据key加分布式锁,锁值:开始时间+超时时间+1毫秒
//     *
//     * @param key       lock_key
//     * @param startTime 开始时间
//     * @return boolean
//     */
//    public boolean getLock(String key, long startTime) {
//        String value = Long.toString(startTime + TIME_OUT + 1);
//        boolean lock = false;
//        try {
//            Boolean isSetNx = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
//            lock = Objects.nonNull(isSetNx) && isSetNx;
//            if (!lock) {
//                String vaStr = stringRedisTemplate.opsForValue().get(key);
//                if (StringUtils.isNotEmpty(vaStr)) {
//                    long oldValue = Long.parseLong(vaStr);
//                    if (oldValue < System.currentTimeMillis()) {
//                        String getValue = stringRedisTemplate.opsForValue().getAndSet(key, value);
//                        if (StringUtils.isNotEmpty(getValue) && (Long.parseLong(getValue) == oldValue)) {
//                            lock = true;
//                        }
//                    }
//                }
//            }
//            log.info("分布式锁 =》{} {}", key, lock);
//        } catch (Exception e) {
//            log.error("分布式锁出错 =》 {}", key, e);
//        }
//        return lock;
//    }
//
//    /**
//     * 根据key删除分布式锁,
//     *
//     * @param key       lock_key
//     * @param startTime 开始时间
//     */
//    public boolean delLock(String key, long startTime) {
//        boolean unLock = false;
//        try {
//            String strVa    = stringRedisTemplate.opsForValue().get(key);
//            long   oldValue = StringUtils.isEmpty(strVa) ? 0 : Long.parseLong(strVa);
//            //        锁值:开始时间+超时时间+1毫秒
//            if (oldValue == startTime + TIME_OUT + 1) {
//                // 避免删除非自己获取得到的锁
//                Boolean isDel = stringRedisTemplate.delete(key);
//                unLock = Objects.nonNull(isDel) && isDel;
//            }
//            log.info("分布式锁 => {}", key);
//        } catch (Exception e) {
//            log.error("分布式锁error => {}", e.getStackTrace(),e);
//        }
//        return unLock;
//    }
//}
