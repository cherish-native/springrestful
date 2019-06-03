package com.dao;

import com.entity.WorkQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface WorkQueueDao extends CrudRepository<WorkQueue,String> {

    Page<WorkQueue> findAll(Specification<WorkQueue> params, Pageable pageable);

    List<WorkQueue> findByOrderByInsertTimeAsc();
}
