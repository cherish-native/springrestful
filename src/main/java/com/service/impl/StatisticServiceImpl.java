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
        sql.append("     select t.depart_code DEPARTCODE, t.gatheruser_name GATHERUSERNAME,")
                .append("sum(count) COUNT,")
                .append("sum(t.count_level_a) COUNTLEVELA,")
                .append("sum(t.count_level_b) COUNTLEVELB,")
                .append("sum(t.count_level_c) COUNTLEVELC,")
                .append("sum(t.count_level_d) COUNTLEVELD,")
                .append("sum(t.count_level_e) COUNTLEVELE ")
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
                statisticQualityDay.setGatheruserName(StringUtils.nvlString(map.get("GATHERUSERNAME")));
                statisticQualityDay.setCount(StringUtils.nvlInt(map.get("COUNT")));
                statisticQualityDay.setCountLevelA(StringUtils.nvlInt(map.get("COUNTLEVELA")));
                statisticQualityDay.setCountLevelB(StringUtils.nvlInt(map.get("COUNTLEVELB")));
                statisticQualityDay.setCountLevelC(StringUtils.nvlInt(map.get("COUNTLEVELC")));
                statisticQualityDay.setCountLevelD(StringUtils.nvlInt(map.get("COUNTLEVELD")));
                statisticQualityDay.setCountLevelE(StringUtils.nvlInt(map.get("COUNTLEVELE")));
                statisticQualityDays.add(statisticQualityDay);
            }
        }
        return new DataGridReturn(statisticQualityDays.size(), statisticQualityDays);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void statisticDay(String dateStr){
        StringBuilder sql = new StringBuilder();
             sql.append("select t.PRINT_UNIT_CODE DEPARTCODE,                                     ")
                .append("   t.PRINTER GATHERUSERID,                                               ")
                .append("   sum(case when t.QUALITY_LEVEL=1 then 1 else 0 end) COUNTLEVELA,       ")
                .append("   sum(case when t.QUALITY_LEVEL=2 then 1 else 0 end) COUNTLEVELB,       ")
                .append("   sum(case when t.QUALITY_LEVEL=3 then 1 else 0 end) COUNTLEVELC,       ")
                .append("   sum(case when t.QUALITY_LEVEL=4 then 1 else 0 end) COUNTLEVELD,       ")
                .append("   sum(case when t.QUALITY_LEVEL=5 then 1 else 0 end) COUNTLEVELE,       ")
                .append("   sum(case when t.IS_COMPEL_PASS=1 then 1 else 0 end) ISCOMPELPASSCOUNT,")
                .append("   sum(case when t.IS_COMPEL_PASS=0 then 1 else 0 end) UNCOMPELPASSCOUNT,")
                .append("   sum(case when t.is_qualified=1 then 1 else 0 end) STANDARDCOUNT,      ")
                .append("   sum(case when t.is_qualified=0 then 1 else 0 end) SUBSTANDARDCOUNT,   ")
                .append("   round(avg(t.TOTAL_SCORE), 1) SCOREAVERAGE,                            ")
                .append("   count(*) COUNT                                                        ")
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
                departCode = StringUtils.nvlString(map.get("DEPARTCODE"));
                gatherUserId = StringUtils.nvlString(map.get("GATHERUSERID"));
                StatisticQualityDay statisticQualityDay = statisticQualityDayDao.findByDepartCodeAndGatheruserIdAndStatisticTime(departCode, gatherUserId, statisticTime);
                if(statisticQualityDay == null){
                    statisticQualityDay = new StatisticQualityDay();
                }
                statisticQualityDay.setDepartCode(departCode);
                statisticQualityDay.setGatheruserId(gatherUserId);
                statisticQualityDay.setStatisticTime(statisticTime);
                statisticQualityDay.setCountLevelA(StringUtils.nvlInt("COUNTLEVELA"));
                statisticQualityDay.setCountLevelB(StringUtils.nvlInt("COUNTLEVELB"));
                statisticQualityDay.setCountLevelC(StringUtils.nvlInt("COUNTLEVELC"));
                statisticQualityDay.setCountLevelD(StringUtils.nvlInt("COUNTLEVELD"));
                statisticQualityDay.setCountLevelE(StringUtils.nvlInt("COUNTLEVELE"));
                statisticQualityDay.setIsCompelPassCount(StringUtils.nvlInt("ISCOMPELPASSCOUNT"));
                statisticQualityDay.setUnCompelPassCount(StringUtils.nvlInt("UNCOMPELPASSCOUNT"));
                statisticQualityDay.setStandardCount(StringUtils.nvlInt("STANDARDCOUNT"));
                statisticQualityDay.setSubstandardCount(StringUtils.nvlInt("SUBSTANDARDCOUNT"));
                statisticQualityDay.setScoreAverage(StringUtils.nvlString("SCOREAVERAGE"));
                statisticQualityDay.setCount(StringUtils.nvlInt("COUNT"));
                statisticQualityDayDao.save(statisticQualityDay);
            }
        }
    }
}
