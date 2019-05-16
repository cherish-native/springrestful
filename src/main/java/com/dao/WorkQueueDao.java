package com.dao;

import com.entity.WorkQueue;
import org.springframework.data.repository.CrudRepository;

public interface WorkQueueDao extends CrudRepository<WorkQueue,String> {
}
