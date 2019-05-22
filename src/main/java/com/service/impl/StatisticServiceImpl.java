package com.service.impl;

import com.constant.Constant;
import com.dao.BaseDao;
import com.dao.StatisticQualityDayDao;
import com.entity.StatisticQualityDay;
import com.entity.SysDepart;
import com.entity.vo.DataGridReturn;
import com.entity.vo.PersonVo;
import com.service.StatisticService;
import com.service.SysDepartService;
import com.util.DateUtil;
import com.util.QueryBuilder;
import com.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
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
    @Autowired
    private SysDepartService sysDepartService;

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
                statisticQualityDay.setDepartCode(StringUtils.nvlString(map.get("DEPARTCODE")));
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
//                .append("   sum(case when t.IS_COMPEL_PASS=1 then 1 else 0 end) ISCOMPELPASSCOUNT,")
                .append("   sum(case when t.IS_COMPEL_PASS='00000000000000000000' then 1 else 0 end) UNCOMPELPASSCOUNT,")
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
                .append("group by t.PRINT_UNIT_CODE, t.PRINTER                                   ");
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
                statisticQualityDay.setDepartName(Constant.codeAndNameDB.get(departCode));
                statisticQualityDay.setGatheruserId(gatherUserId);
                statisticQualityDay.setGatheruserName(gatherUserId);
                statisticQualityDay.setStatisticTime(statisticTime);
                statisticQualityDay.setCountLevelA(StringUtils.nvlInt(map.get("COUNTLEVELA")));
                statisticQualityDay.setCountLevelB(StringUtils.nvlInt(map.get("COUNTLEVELB")));
                statisticQualityDay.setCountLevelC(StringUtils.nvlInt(map.get("COUNTLEVELC")));
                statisticQualityDay.setCountLevelD(StringUtils.nvlInt(map.get("COUNTLEVELD")));
                statisticQualityDay.setCountLevelE(StringUtils.nvlInt(map.get("COUNTLEVELE")));
//                statisticQualityDay.setIsCompelPassCount(StringUtils.nvlInt("ISCOMPELPASSCOUNT"));
                statisticQualityDay.setCount(StringUtils.nvlInt(map.get("COUNT")));
                statisticQualityDay.setUnCompelPassCount(StringUtils.nvlInt(map.get("UNCOMPELPASSCOUNT")));
                statisticQualityDay.setIsCompelPassCount(statisticQualityDay.getCount() - statisticQualityDay.getUnCompelPassCount());
                statisticQualityDay.setStandardCount(StringUtils.nvlInt(map.get("STANDARDCOUNT")));
                statisticQualityDay.setSubstandardCount(StringUtils.nvlInt(map.get("SUBSTANDARDCOUNT")));
                statisticQualityDay.setScoreAverage(StringUtils.nvlString(map.get("SCOREAVERAGE")));

                statisticQualityDayDao.save(statisticQualityDay);
            }
        }
    }

    @Override
    public DataGridReturn gatherSubstandardExamineList(String departCode, String beginDate, String endDate, Pageable pagination) {
        StringBuilder sql = new StringBuilder();
        sql.append("     select t.depart_code DEPARTCODE, t.gatheruser_name GATHERUSERNAME,")
                .append("sum(count) COUNT,")
                .append("sum(t.substandard_count) SUBSTANDARDCOUNT")
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
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            for(Map<String, Object> map : list){
                StatisticQualityDay statisticQualityDay = new StatisticQualityDay();
                statisticQualityDay.setDepartName(Constant.codeAndNameDB.get(StringUtils.nvlString(map.get("DEPARTCODE"))));
                statisticQualityDay.setGatheruserName(StringUtils.nvlString(map.get("GATHERUSERNAME")));
                statisticQualityDay.setDepartCode(StringUtils.nvlString(map.get("DEPARTCODE")));
                statisticQualityDay.setCount(StringUtils.nvlInt(map.get("COUNT")));
                statisticQualityDay.setSubstandardCount(StringUtils.nvlInt(map.get("SUBSTANDARDCOUNT")));
                statisticQualityDay.setSubstandardPercent(df.format((float)statisticQualityDay.getSubstandardCount()/statisticQualityDay.getCount()*100) + "%");
                statisticQualityDays.add(statisticQualityDay);
            }
        }
        return new DataGridReturn(statisticQualityDays.size(), statisticQualityDays);
    }

    @Override
    public DataGridReturn gatherSubstandardExamineDetailList(String departCode, String gatheruserName, String beginDate, String endDate, Pageable pagination) {
        StringBuilder sql = new StringBuilder("SELECT q.*,t.personid,t.name, t.idcard,t.person_level,t.printdate FROM personinfo t left join quality_score q on t.personid = q.cardid ");
        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        queryBuilder.appendAndWhere(" t.printdate is not null ", null);
        if(StringUtils.isNotEmpty(departCode)){
            queryBuilder.appendAndWhere(" t.print_unit_code = ? ", departCode);
        }
        if(StringUtils.isNotEmpty(gatheruserName)){
            try {
                gatheruserName = new String(gatheruserName.getBytes("ISO-8859-1"),"utf-8");
            } catch (Exception e){
                e.printStackTrace();
            }
            queryBuilder.appendAndWhere(" t.printer = ? ", gatheruserName);
        }
        if(StringUtils.isNotEmpty(beginDate)){
            queryBuilder.appendAndWhere(" t.printdate >= ? ", beginDate);
        }
        if(StringUtils.isNotEmpty(endDate)){
            queryBuilder.appendAndWhere(" t.printdate <= ? ", endDate);
        }
        DataGridReturn dataGridReturn = baseDao.pageQuery(queryBuilder.getSql(), queryBuilder.getParams(), pagination);
        List<PersonVo> personVos = new ArrayList<>();
        List<Map<String, Object>> list = dataGridReturn.getRows();
        if(list != null && list.size() > 0){
            for(Map<String, Object> map : list){
                PersonVo personVo = new PersonVo();
                personVo.setPersonId(StringUtils.nvlString(map.get("PERSONID")));
                personVo.setName(StringUtils.nvlString(map.get("NAME")));
                personVo.setIdCardNo(StringUtils.nvlString(map.get("IDCARD")));
                personVo.setPrintDate(StringUtils.nvlString(map.get("PRINTDATE")));
                personVo.setPersonLevel(StringUtils.nvlString(map.get("PERSON_LEVEL")));
                personVo.setRightThumb(StringUtils.nvlInt(map.get("RMP")) + "/" + StringUtils.nvlInt(map.get("RMR")));
                personVo.setRightIndex(StringUtils.nvlInt(map.get("RSP")) + "/" + StringUtils.nvlInt(map.get("RSR")));
                personVo.setRightMiddle(StringUtils.nvlInt(map.get("RZP")) + "/" + StringUtils.nvlInt(map.get("RZR")));
                personVo.setRightRing(StringUtils.nvlInt(map.get("RHP")) + "/" + StringUtils.nvlInt(map.get("RHR")));
                personVo.setRightLittle(StringUtils.nvlInt(map.get("RXP")) + "/" + StringUtils.nvlInt(map.get("RXR")));
                personVo.setLeftThumb(StringUtils.nvlInt(map.get("LMP")) + "/" + StringUtils.nvlInt(map.get("LMR")));
                personVo.setLeftIndex(StringUtils.nvlInt(map.get("LSP")) + "/" + StringUtils.nvlInt(map.get("LSR")));
                personVo.setLeftMiddle(StringUtils.nvlInt(map.get("LZP")) + "/" + StringUtils.nvlInt(map.get("LZR")));
                personVo.setLeftRing(StringUtils.nvlInt(map.get("LHP")) + "/" + StringUtils.nvlInt(map.get("LHR")));
                personVo.setLeftLittle(StringUtils.nvlInt(map.get("LXP")) + "/" + StringUtils.nvlInt(map.get("LXR")));
                personVo.setImgUrl(StringUtils.nvlString(map.get("IMG_URL")));
                personVo.setQualityLevel(StringUtils.nvlString(map.get("QUALITY_LEVEL")));
                personVos.add(personVo);
            }
        }
        dataGridReturn.setRows(personVos);
        return dataGridReturn;
    }
}
