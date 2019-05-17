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
            queryBuilder.appendAndWhere("t.statistic_time >= ?", Integer.parseInt(beginDate.replaceAll("-", "")));
        }
        if(StringUtils.isNotEmpty(endDate)){
            queryBuilder.appendAndWhere("t.statistic_time <= ?", Integer.parseInt(endDate.replaceAll("-", "")));
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void statisticDay(String dateStr){
        StringBuilder sql = new StringBuilder();
             sql.append("select t.PRINT_UNIT_CODE departCode,                                     ")
                .append("   t.PRINTER gatheruserId,                                               ")
                .append("   sum(case when t.QUALITY_LEVEL=1 then 1 else 0 end) countLevelA,       ")
                .append("   sum(case when t.QUALITY_LEVEL=2 then 1 else 0 end) countLevelB,       ")
                .append("   sum(case when t.QUALITY_LEVEL=3 then 1 else 0 end) countLevelC,       ")
                .append("   sum(case when t.QUALITY_LEVEL=4 then 1 else 0 end) countLevelD,       ")
                .append("   sum(case when t.QUALITY_LEVEL=5 then 1 else 0 end) countLevelE,       ")
                .append("   sum(case when t.IS_COMPEL_PASS=1 then 1 else 0 end) isCompelPassCount,")
                .append("   sum(case when t.IS_COMPEL_PASS=0 then 1 else 0 end) unCompelPassCount,")
                .append("   sum(case when t.is_qualified=1 then 1 else 0 end) standardCount,      ")
                .append("   sum(case when t.is_qualified=0 then 1 else 0 end) substandardCount,   ")
                .append("   round(avg(t.TOTAL_SCORE), 1) scoreAverage,                            ")
                .append("   count(*) count                                                        ")
                .append("from (select p.PRINT_UNIT_CODE,                                          ")
                .append("           p.PRINTER,                                                    ")
                .append("           p.IS_COMPEL_PASS,                                             ")
                .append("           q.TOTAL_SCORE,                                                ")
                .append("           q.QUALITY_LEVEL,                                              ")
                .append("           p.is_qualified                                                ")
                .append("      from personinfo p                                                  ")
                .append("      left join QUALITY_SCORE q                                          ")
                .append("        on p.PERSONID = q.CARDID                                         ")
                .append("     where p.PRINTDATE = ?                                      ")
                .append("     ) t                                                                 ")
                .append("group by t.PRINT_UNIT_CODE, t.PRINTER;                                   ");
        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        queryBuilder.getParams().add(dateStr);
        List<Map<String, Object>> list = baseDao.findListBySql(queryBuilder.getSql(), queryBuilder.getParams());
        if(list != null && list.size() > 0){
            Map<String, Object> map = null;
            String departCode = null;
            String gatherUserId = null;
            Integer statisticTime = Integer.parseInt(dateStr);
            for(int i=0;i<list.size();i++){
                map = list.get(i);
                departCode = StringUtils.nvlString(map.get("departCode"));
                gatherUserId = StringUtils.nvlString(map.get("gatheruserId"));
                StatisticQualityDay statisticQualityDay = statisticQualityDayDao.findByDepartCodeAndGatheruserIdAndStatisticTime(departCode, gatherUserId, statisticTime);
                if(statisticQualityDay == null){
                    statisticQualityDay = new StatisticQualityDay();
                }
                statisticQualityDay.setDepartCode(departCode);
                statisticQualityDay.setGatheruserId(gatherUserId);
                statisticQualityDay.setStatisticTime(statisticTime);
                statisticQualityDay.setCountLevelA(StringUtils.nvlInt("countLevelA"));
                statisticQualityDay.setCountLevelB(StringUtils.nvlInt("countLevelB"));
                statisticQualityDay.setCountLevelC(StringUtils.nvlInt("countLevelC"));
                statisticQualityDay.setCountLevelD(StringUtils.nvlInt("countLevelD"));
                statisticQualityDay.setCountLevelE(StringUtils.nvlInt("countLevelE"));
                statisticQualityDay.setIsCompelPassCount(StringUtils.nvlInt("isCompelPassCount"));
                statisticQualityDay.setUnCompelPassCount(StringUtils.nvlInt("unCompelPassCount"));
                statisticQualityDay.setStandardCount(StringUtils.nvlInt("standardCount"));
                statisticQualityDay.setSubstandardCount(StringUtils.nvlInt("substandardCount"));
                statisticQualityDay.setScoreAverage(StringUtils.nvlString("scoreAverage"));
                statisticQualityDay.setCount(StringUtils.nvlInt("count"));
                statisticQualityDayDao.save(statisticQualityDay);
            }
        }
    }
}
