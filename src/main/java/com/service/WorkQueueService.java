package com.service;

import com.entity.WorkQueue;

import java.util.List;

public interface WorkQueueService {
    /**
     * 获取所有工作队列
     * @return
     */
    WorkQueue getWorkQueueById(String id);

    void save(WorkQueue workQueue);

    List<WorkQueue> getWorkQueueWorkState(int[] stateArr);

    /**
     * 获取工作队列中的最后一个任务
     * @return
     */
    WorkQueue getLastWorkQueue();
}
