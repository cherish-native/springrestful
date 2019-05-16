package com.service;

import com.entity.WorkQueue;

import java.util.List;

public interface WorkQueueService {
    /**
     * 获取所有工作队列
     * @return
     */
    WorkQueue getWorkQueueById(String id);
}
