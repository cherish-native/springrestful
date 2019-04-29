package com.service.impl;

import com.dao.StatisticQualityDayDao;
import com.entity.StatisticQualityDay;
import com.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private StatisticQualityDayDao statisticQualityDayDao;

    @Override
    public Map<String, Object> getImageQualityStatisticsDay(int date) {
        Map<String, Object> tempMap = new HashMap<>();
        List<StatisticQualityDay> statisticQualityDays = statisticQualityDayDao.findByStatisticTime(date);
        if(statisticQualityDays != null && statisticQualityDays.size() > 0){
            for(StatisticQualityDay statisticQualityDay : statisticQualityDays){
                tempMap.put(statisticQualityDay.getDepartName(), statisticQualityDay);
            }
        }
        return tempMap;
    }
}
