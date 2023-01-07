package com.xingdong.seckill.graphql.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 程征波
 * @date 2021/11/13
 */
public class ThreadUtils {

    public static ThreadPoolExecutor getThread(String name) {
        int                      processors = Runtime.getRuntime().availableProcessors();
        RejectedExecutionHandler handler    = new ThreadPoolExecutor.CallerRunsPolicy();
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("#####graphql####thread#### =>" + name).setDaemon(true).build();
        return new ThreadPoolExecutor(
                processors, processors * 2,
                0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                handler
        );
    }

}
