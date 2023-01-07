package com.xingdong.seckill.common.utils;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author chengzhengzheng
 * @date 2021/12/30
 * <p>
 * 提供一种内存线程安全的buffer队列
 *
 * 参考文章: https://segmentfault.com/a/1190000021962190
 */
public class MemoryBufferPool<K, V> {

    private final Map<K, Queue<V>> MEMORY_BUFFER = new ConcurrentHashMap<>();

    private ReadWriteLock rwl = new ReentrantReadWriteLock();

    public void offer(K key, V value) {

        Queue<V> queue;

        rwl.readLock().lock();


        try {
            if (!MEMORY_BUFFER.containsKey(key)) {
                //开始写之前先释放读锁
                rwl.readLock().unlock();
                //获取写锁开始写入数据
                rwl.writeLock().lock();

                try {
                    if (!MEMORY_BUFFER.containsKey(key)) {
                        queue = new ConcurrentLinkedDeque<>();
                        MEMORY_BUFFER.put(key, queue);
                    }
                    //通过在释放写锁之前获取读锁来进行锁的降级
                    rwl.readLock().lock();
                } finally {
                    rwl.writeLock().unlock();
                }
            }
            //这里没有加写锁的原因
            MEMORY_BUFFER.get(key).offer(value);
        } finally {
            rwl.readLock().unlock();
        }
    }


    public V poll(K key) {
        if (MEMORY_BUFFER.get(key) == null) {
            return null;
        }
        return MEMORY_BUFFER.get(key).poll();
    }
}
