package com.service.impl;

import com.constant.Constant;
import com.dao.BaseDao;
import com.dao.StatisticQualityDayDao;
import com.entity.StatisticQualityDay;
import com.entity.vo.DataGridReturn;
import com.service.StatisticService;
import com.util.DateUtil;
import com.util.QueryBuilder;
import com.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private BaseDao baseDao;

    @Override
    public Map<String, Object> getImageQualityStatisticsDay(int date) {
        Map<String, Object> tempMap = new HashMap<>();
        List<StatisticQualityDay> statisticQualityDays = statisticQualityDayDao.findByStatisticTime(date);
        if(statisticQualityDays != null && statisticQualityDays.size() > 0){
            for(StatisticQualityDay statisticQualityDay : statisticQualityDays){
                tempMap.put(Constant.departCodeNameMap.get(statisticQualityDay.getDepartName()), statisticQualityDay);
            }
        }
        return tempMap;
    }

    @Override
    public DataGridReturn getGatherQualityExamineList(String departCode, String beginDate, String endDate, Pageable pageable) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("     select t.depart_code departCode, t.gatheruser_name gatheruserName,")
                .append("sum(count) count,")
                .append("sum(t.count_level_a) countLevelA,")
                .append("sum(t.count_level_b) countLevelB,")
                .append("sum(t.count_level_c) countLevelC,")
                .append("sum(t.count_level_d) countLevelD,")
                .append("sum(t.count_level_e) countLevelE ")
                .append(" from statistic_quality_day t");
        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        if(StringUtils.isNotEmpty(departCode)){
            queryBuilder.appendAndWhere("t.depart_code like ?", departCode);
        }
        if(StringUtils.isNotEmpty(beginDate)){
            queryBuilder.appendAndWhere("t.statistic_time >= ?", DateUtil.strToDate(beginDate + " 00:00:00", DateUtil.PATTERN_YYYYMMDD_WITH_HORIZONTAL_LINE));
        }
        if(StringUtils.isNotEmpty(endDate)){
            queryBuilder.appendAndWhere("t.statistic_time <= ?", DateUtil.strToDate(endDate + " 23:59:59", DateUtil.PATTERN_DATETIME));
        }
        queryBuilder.appendSql("group by t.depart_code, t.gatheruser_name");
        List<Map<String, Object>> list = baseDao.findListBySql(queryBuilder.getSql(), queryBuilder.getParams());
        List<StatisticQualityDay> statisticQualityDays = new ArrayList<>();
        if(list != null){
            for(Map<String, Object> map : list){
                StatisticQualityDay statisticQualityDay = new StatisticQualityDay();
                statisticQualityDay.setDepartName(statisticQualityDay.getDepartName());
                statisticQualityDay.setGatheruserName(StringUtils.nvlString(map.get("gatheruserName")));
                statisticQualityDay.setCount(StringUtils.nvlInt(map.get("count")));
                statisticQualityDay.setCountLevelA(StringUtils.nvlInt(map.get("countLevelA")));
                statisticQualityDay.setCountLevelB(StringUtils.nvlInt(map.get("countLevelB")));
                statisticQualityDay.setCountLevelC(StringUtils.nvlInt(map.get("countLevelC")));
                statisticQualityDay.setCountLevelD(StringUtils.nvlInt(map.get("countLevelD")));
                statisticQualityDay.setCountLevelE(StringUtils.nvlInt(map.get("countLevelE")));
                statisticQualityDays.add(statisticQualityDay);
            }
        }
        return new DataGridReturn(statisticQualityDays.size(), statisticQualityDays);
    }
}
