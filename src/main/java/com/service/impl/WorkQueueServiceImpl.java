package com.service.impl;

import com.dao.WorkQueueDao;
import com.entity.WorkQueue;
import com.service.WorkQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkQueueServiceImpl implements WorkQueueService {

    @Autowired
    private WorkQueueDao workQueueDao;

    @Override
    public WorkQueue getWorkQueueById(String id){
        return workQueueDao.findOne(id);
    }
}
