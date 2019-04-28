package com.service;

import com.entity.StatisticQualityDay;

import java.util.List;
import java.util.Map;

public interface StatisticService {

    /**
     * 获取每日图像质量统计结果
     * @param date
     * @return
     */
    Map<String, Object> getImageQualityStatisticsDay(int date);

}
