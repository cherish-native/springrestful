package com.dao;

import com.entity.StatisticQualityDay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatisticQualityDayDao extends CrudRepository<StatisticQualityDay,String> {

    /**
     * 根据时间获取当日所有统计数据
     * @param statisticTime
     * @return
     */
    List<StatisticQualityDay> findByStatisticTime(int statisticTime);

    /**
     * 分页查询
     * @return
     */
    Page<StatisticQualityDay> findAll(Specification<StatisticQualityDay> params, Pageable pageable);

}
