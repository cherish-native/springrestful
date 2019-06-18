package com.service.impl;

import com.dao.WorkQueueDao;
import com.entity.WorkQueue;
import com.service.WorkQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(WorkQueue workQueue) {
        workQueueDao.save(workQueue);
    }

    @Override
    public List<WorkQueue> getWorkQueueWorkState(int[] stateArr) {
        Pageable pageable = new PageRequest(0, 30);
        Specification<WorkQueue> params = new Specification<WorkQueue>() {
            @Override
            public Predicate toPredicate(Root<WorkQueue> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                Path<Object> path = root.get("workState");
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                for(int state : stateArr){
                    in.value(state);
                }
                predicates.add(criteriaBuilder.and(in));


                if(predicates.size() > 0){
                    Predicate[] predicatesArray = predicates.toArray (new Predicate[predicates.size()]);
                    criteriaQuery.where(criteriaBuilder.or(predicatesArray));
                }
                return criteriaQuery.getRestriction();
            }
        };
        Page<WorkQueue> page = workQueueDao.findAll(params,pageable);
        return page.getContent();
    }

    @Override
    public WorkQueue getLastWorkQueue() {
        Sort sort = new Sort(Sort.Direction.DESC, "insertTime");
        List<WorkQueue> workQueueList = workQueueDao.findByOrderByInsertTimeDesc();
        if(workQueueList != null && workQueueList.size() > 0){
            return workQueueList.get(0);
        }else{
            return null;
        }
    }

}
