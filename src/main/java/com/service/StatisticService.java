package com.service;

import com.entity.StatisticQualityDay;
import com.entity.vo.DataGridReturn;
import com.entity.vo.Pagination;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface StatisticService {

    /**
     * 获取每日图像质量统计结果
     * @param date
     * @return
     */
    Map<String, Object> getImageQualityStatisticsDay(int date);

    /**
     * 获取采集质量考核列表
     * @param departCode
     * @param beginDate
     * @param endDate
     * @param pageable
     * @return
     */
    DataGridReturn getGatherQualityExamineList(String departCode, String beginDate, String endDate, Pageable pageable) throws Exception;
}
