package com.system.design.seckil.common.job;


import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chengzhengzheng
 * @date 2021/9/21
 */
@Service
public class ScheduleJob {
    private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
    //TODO 后期改为分布式任务调度组件
    public void addTask(long delayed,Runnable tasks) {
        scheduler.schedule(tasks,delayed, TimeUnit.MINUTES);
    }
}
