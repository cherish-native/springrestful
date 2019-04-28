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
                StatisticQualityDay temp = (StatisticQualityDay)tempMap.get(statisticQualityDay.getDepartName());
                if(temp == null){
                    tempMap.put(statisticQualityDay.getDepartName(), statisticQualityDay);
                }else{
                    //非空表示同时存在强制通过和非强制通过的数据，需统计总数和平均分
                    float totalScore = statisticQualityDay.getCount() * Float.parseFloat(statisticQualityDay.getScoreAverage());
                    float totalScoreTemp = statisticQualityDay.getCount() * Float.parseFloat(temp.getScoreAverage());
                    int scoreAverage = (int)((totalScore + totalScoreTemp) / (statisticQualityDay.getCount() + temp.getCount()));
                    temp.setCount(statisticQualityDay.getCount() + temp.getCount());
                    temp.setScoreAverage(scoreAverage+"");
                    temp.setCountLevelA(temp.getCountLevelA() + statisticQualityDay.getCountLevelA());
                    temp.setCountLevelB(temp.getCountLevelB() + statisticQualityDay.getCountLevelB());
                    temp.setCountLevelC(temp.getCountLevelC() + statisticQualityDay.getCountLevelC());
                    temp.setCountLevelD(temp.getCountLevelD() + statisticQualityDay.getCountLevelD());
                    temp.setCountLevelE(temp.getCountLevelE() + statisticQualityDay.getCountLevelE());
                    tempMap.put(statisticQualityDay.getDepartName(), temp);
                }
            }
        }
        return tempMap;
    }
}
