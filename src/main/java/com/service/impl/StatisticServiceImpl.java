package com.service.impl;

import com.constant.Constant;
import com.dao.BaseDao;
import com.dao.StatisticQualityDayDao;
import com.entity.StatisticQualityDay;
import com.entity.SysUser;
import com.entity.vo.DataGridReturn;
import com.entity.vo.ImageSubstandardStatistics;
import com.entity.vo.PersonVo;
import com.service.StatisticService;
import com.service.SysDepartService;
import com.util.QueryBuilder;
import com.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                if(StringUtils.isNotEmpty(statisticQualityDay.getScoreAverage())){
                    StatisticQualityDay temp = (StatisticQualityDay) tempMap.get(statisticQualityDay.getDepartName());
                    if(temp == null){
                        tempMap.put(Constant.departCodeNameMap.get(statisticQualityDay.getDepartCode()), statisticQualityDay);
                    }else{
                        //计算已有的总数和平均分
                        float totalScore = temp.getCount()*Float.parseFloat(temp.getScoreAverage()) + statisticQualityDay.getCount()*Float.parseFloat(statisticQualityDay.getScoreAverage());
                        int count = temp.getCount() + statisticQualityDay.getCount();
                        float averageScore = totalScore/count;
                        temp.setCount(count);
                        temp.setScoreAverage(averageScore+"");
                        temp.setCountLevelA(temp.getCountLevelA()+statisticQualityDay.getCountLevelA());
                        temp.setCountLevelA(temp.getCountLevelB()+statisticQualityDay.getCountLevelB());
                        temp.setCountLevelA(temp.getCountLevelC()+statisticQualityDay.getCountLevelC());
                        temp.setCountLevelA(temp.getCountLevelD()+statisticQualityDay.getCountLevelD());
                        temp.setCountLevelA(temp.getCountLevelE()+statisticQualityDay.getCountLevelE());
                    }
                }
            }
        }
        return tempMap;
    }

    @Override
    public DataGridReturn getGatherQualityExamineList(String departCode, String beginDate, String endDate, Pageable pageable, SysUser sysUser) throws Exception {
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
        if(Constant.TOP_DEPARTCODE.equals(sysUser.getUnitCode())){
            if(StringUtils.isNotEmpty(departCode)){
                queryBuilder.appendAndWhere("t.depart_code like ?", departCode);
            }
        }else{
            queryBuilder.appendAndWhere("t.depart_code = ?", sysUser.getUnitCode());
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
                statisticQualityDay.setDepartCode(StringUtils.nvlString(map.get("DEPARTCODE")));
                statisticQualityDay.setDepartName(Constant.codeAndNameDB.get(statisticQualityDay.getDepartCode()));
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
                .append("     where p.PRINTDATE = ? and p.PRINTER is not null                     ")
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

    public static void main(String[] args) throws Exception{
        String o = "%E5%94%90%E5%AF%85%E6%9D%B0";
        String gatheruserName = java.net.URLDecoder.decode(o,"utf-8");
        System.out.println(gatheruserName);
    }

    @Override
    public DataGridReturn gatherSubstandardExamineList(String departCode, String beginDate, String endDate, Pageable pagination, SysUser sysUser) {
        StringBuilder sql = new StringBuilder();
        sql.append("     select t.depart_code DEPARTCODE, t.gatheruser_name GATHERUSERNAME,")
                .append("sum(count) COUNT,")
                .append("sum(t.substandard_count) SUBSTANDARDCOUNT")
                .append(" from statistic_quality_day t");
        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        if(Constant.TOP_DEPARTCODE.equals(sysUser.getUnitCode())){
            if(StringUtils.isNotEmpty(departCode)){
                queryBuilder.appendAndWhere("t.depart_code like ?", departCode);
            }
        }else{
            queryBuilder.appendAndWhere("t.depart_code = ?", sysUser.getUnitCode());
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
    public DataGridReturn gatherSubstandardExamineDetailList(String departCode, String gatheruserName, String beginDate, String endDate, Pageable pagination, SysUser sysUser) {
        StringBuilder sql = new StringBuilder("SELECT q.*,t.personid,t.name, t.idcard,t.person_level,t.printdate FROM personinfo t left join quality_score q on t.personid = q.cardid ");
        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        queryBuilder.appendAndWhere(" t.printdate is not null ", null);
        //默认查询未达标数据
        queryBuilder.appendAndWhere(" t.is_qualified = ? ", 0);
        if(Constant.TOP_DEPARTCODE.equals(sysUser.getUnitCode())){
            if(StringUtils.isNotEmpty(departCode)){
                queryBuilder.appendAndWhere("t.print_unit_code = ?", departCode);
            }
        }else{
            queryBuilder.appendAndWhere("t.print_unit_code = ?", sysUser.getUnitCode());
        }

        if(StringUtils.isNotEmpty(gatheruserName)){
            try {
                gatheruserName = java.net.URLDecoder.decode(gatheruserName,"utf-8");
            } catch (Exception e){
                e.printStackTrace();
            }
            queryBuilder.appendAndWhere(" t.printer = ? ", gatheruserName);
        }
        if(StringUtils.isNotEmpty(beginDate)){
            queryBuilder.appendAndWhere(" t.printdate >= ? ", beginDate.replaceAll("-",""));
        }
        if(StringUtils.isNotEmpty(endDate)){
            queryBuilder.appendAndWhere(" t.printdate <= ? ", endDate.replaceAll("-",""));
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
                personVo.setRightThumb(StringUtils.nvlFloatWithDecimal2(map.get("RMP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("RMR")));
                personVo.setRightIndex(StringUtils.nvlFloatWithDecimal2(map.get("RSP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("RSR")));
                personVo.setRightMiddle(StringUtils.nvlFloatWithDecimal2(map.get("RZP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("RZR")));
                personVo.setRightRing(StringUtils.nvlFloatWithDecimal2(map.get("RHP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("RHR")));
                personVo.setRightLittle(StringUtils.nvlFloatWithDecimal2(map.get("RXP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("RXR")));
                personVo.setLeftThumb(StringUtils.nvlFloatWithDecimal2(map.get("LMP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("LMR")));
                personVo.setLeftIndex(StringUtils.nvlFloatWithDecimal2(map.get("LSP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("LSR")));
                personVo.setLeftMiddle(StringUtils.nvlFloatWithDecimal2(map.get("LZP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("LZR")));
                personVo.setLeftRing(StringUtils.nvlFloatWithDecimal2(map.get("LHP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("LHR")));
                personVo.setLeftLittle(StringUtils.nvlFloatWithDecimal2(map.get("LXP")) + "/" + StringUtils.nvlFloatWithDecimal2(map.get("LXR")));
                personVo.setImgUrl(StringUtils.nvlString(map.get("IMG_URL")));
                personVo.setQualityLevel(StringUtils.nvlString(map.get("QUALITY_LEVEL")));
                personVos.add(personVo);
            }
        }
        dataGridReturn.setRows(personVos);
        return dataGridReturn;
    }

    @Override
    public DataGridReturn gatherCompelPassList(String departCode, String beginDate, String endDate, Pageable pagination, SysUser sysUser) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.depart_code DEPARTCODE,                   ")
                .append("        t.gatheruser_name GATHERUSERNAME,           ")
                .append("        sum(count) COUNT,                           ")
                .append("        sum(t.is_compel_pass_count) COMPELPASSCOUNT")
                .append("   from statistic_quality_day t                     ");

        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        if(Constant.TOP_DEPARTCODE.equals(sysUser.getUnitCode())){
            if(StringUtils.isNotEmpty(departCode)){
                queryBuilder.appendAndWhere("t.depart_code like ?", departCode);
            }
        }else{
            queryBuilder.appendAndWhere("t.depart_code = ?", sysUser.getUnitCode());
        }
        if(StringUtils.isNotEmpty(beginDate)){
            queryBuilder.appendAndWhere("t.statistic_time >= ?", Integer.parseInt(beginDate.replaceAll("-", "")));
        }
        if(StringUtils.isNotEmpty(endDate)){
            queryBuilder.appendAndWhere("t.statistic_time <= ?", Integer.parseInt(endDate.replaceAll("-", "")));
        }
        queryBuilder.appendSql("  GROUP BY t.depart_code, t.gatheruser_name ");
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
                statisticQualityDay.setIsCompelPassCount(StringUtils.nvlInt(map.get("COMPELPASSCOUNT")));
                statisticQualityDay.setCompelPassPercent(df.format((float)statisticQualityDay.getIsCompelPassCount()/statisticQualityDay.getCount()*100) + "%");
                statisticQualityDays.add(statisticQualityDay);
            }
        }
        return new DataGridReturn(statisticQualityDays.size(), statisticQualityDays);
    }

    @Override
    public DataGridReturn gatherSubstandardCompelPassDetailList(String departCode, String gatheruserName, String beginDate, String endDate, Pageable pagination) {
        StringBuilder sql = new StringBuilder("SELECT q.*,t.personid,t.name, t.idcard,t.person_level,t.printdate,is_compel_pass iscompelpass FROM personinfo t left join quality_score q on t.personid = q.cardid ");
        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        queryBuilder.appendAndWhere(" t.printdate is not null ", null);
        //默认查询强制通过数据
        queryBuilder.appendAndWhere(" t.is_compel_pass != ? ", "00000000000000000000");
        if(StringUtils.isNotEmpty(departCode)){
            queryBuilder.appendAndWhere(" t.print_unit_code = ? ", departCode);
        }
        if(StringUtils.isNotEmpty(gatheruserName)){
            try {
                gatheruserName = java.net.URLDecoder.decode(gatheruserName,"utf-8");
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
        List<Map<String, Object>> list = dataGridReturn.getRows();
        List<PersonVo> personVos = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(Map<String, Object> map : list){
                PersonVo personVo = new PersonVo();
                personVo.setPersonId(StringUtils.nvlString(map.get("PERSONID")));
                personVo.setName(StringUtils.nvlString(map.get("NAME")));
                personVo.setIdCardNo(StringUtils.nvlString(map.get("IDCARD")));
                personVo.setPrintDate(StringUtils.nvlString(map.get("PRINTDATE")));
                personVo.setPersonLevel(StringUtils.nvlString(map.get("PERSON_LEVEL")));
                String iscompelpass = StringUtils.nvlString(map.get("ISCOMPELPASS"));
                if(StringUtils.isNotEmpty(iscompelpass)){
                    //强制通过解析，长度为20，每一位对应一个指位 滚动右拇指--平面左小指，每位由0和1表示
                    for(int i=0;i<10;i++){
                        personVo.setRightThumb(getIsCompelPass(iscompelpass.charAt(0), iscompelpass.charAt(10)) +"");
                        personVo.setRightIndex(getIsCompelPass(iscompelpass.charAt(1), iscompelpass.charAt(11)) +"");
                        personVo.setRightMiddle(getIsCompelPass(iscompelpass.charAt(2), iscompelpass.charAt(12)) +"");
                        personVo.setRightRing(getIsCompelPass(iscompelpass.charAt(3), iscompelpass.charAt(13)) +"");
                        personVo.setRightLittle(getIsCompelPass(iscompelpass.charAt(4), iscompelpass.charAt(14)) +"");
                        personVo.setLeftThumb(getIsCompelPass(iscompelpass.charAt(5), iscompelpass.charAt(15)) +"");
                        personVo.setLeftIndex(getIsCompelPass(iscompelpass.charAt(6), iscompelpass.charAt(16)) +"");
                        personVo.setLeftMiddle(getIsCompelPass(iscompelpass.charAt(7), iscompelpass.charAt(17)) +"");
                        personVo.setLeftRing(getIsCompelPass(iscompelpass.charAt(8), iscompelpass.charAt(18)) +"");
                        personVo.setLeftLittle(getIsCompelPass(iscompelpass.charAt(9), iscompelpass.charAt(19)) +"");
                        personVo.setImgUrl(StringUtils.nvlString(map.get("IMG_URL")));
                    }
                }
                personVos.add(personVo);
            }
        }
        dataGridReturn.setRows(personVos);
        return dataGridReturn;
    }

    @Override
    public List<int[]> getGatherQualityCount(String dateStr,int xAxisCount,String departCode) throws Exception {
        List<int[]> yAxisData = new ArrayList<>();
        int[] yAxisDataA= new int[xAxisCount];
        int[] yAxisDataB= new int[xAxisCount];
        int[] yAxisDataC= new int[xAxisCount];
        int[] yAxisDataD= new int[xAxisCount];
        int[] yAxisDataE= new int[xAxisCount];

        int minQueryParam;
        int maxQueryParam;

        StringBuilder sql = new StringBuilder();
        sql.append("select sum(t.count_level_a) countLevelA,")
                .append("sum(t.count_level_b) countLevelB,")
                .append("sum(t.count_level_c) countLevelC,")
                .append("sum(t.count_level_d) countLevelD,")
                .append("sum(t.count_level_e) countLevelE, ");
                
        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        
        if(xAxisCount==12){
        	queryBuilder.appendSql(" substr(t.statistic_time,0,6) statistic_time");
        	queryBuilder.appendSql(" from statistic_quality_day t");
        	if(StringUtils.isNotEmpty(departCode)){
            	queryBuilder.appendAndWhere(" t.depart_code = ? ", departCode);
        	}
            queryBuilder.appendSql("group by substr(t.statistic_time,0,6)");
            //设置年份查询条件
        	String year = dateStr.substring(0,4);
            minQueryParam = Integer.parseInt(year+"01");
            maxQueryParam = Integer.parseInt(year+"12");
            if(StringUtils.isNotEmpty(minQueryParam)){
                queryBuilder.appendAndHaving("substr(t.statistic_time,0,6) >= ?", minQueryParam);
            }
            if(StringUtils.isNotEmpty(maxQueryParam)){
                queryBuilder.appendAndHaving("substr(t.statistic_time,0,6) <= ?", maxQueryParam);
            }
        }else{
        	queryBuilder.appendSql("t.statistic_time statistic_time ");
        	queryBuilder.appendSql(" from statistic_quality_day t");
        	if(StringUtils.isNotEmpty(departCode)){
            	queryBuilder.appendAndWhere(" t.depart_code = ? ", departCode);
        	}
            queryBuilder.appendSql("group by t.statistic_time");

        	//获取当月的第一天和最后一天
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
            Calendar cale = Calendar.getInstance(); 
            cale.set(Calendar.YEAR,Integer.parseInt(dateStr.substring(0,4)));
            cale.set(Calendar.MONTH,Integer.parseInt(dateStr.substring(5,dateStr.length()))-1);           
            int firstDay = cale.getActualMinimum(Calendar.DAY_OF_MONTH);
            int lastDay = cale.getActualMaximum(Calendar.DAY_OF_MONTH);
   			cale.set(Calendar.DAY_OF_MONTH, firstDay);
   			String firstDayOfMonth = sdf.format(cale.getTime());
            minQueryParam = Integer.parseInt(firstDayOfMonth);
   			cale.set(Calendar.DAY_OF_MONTH, lastDay);
   			String lastDayOfMonth = sdf.format(cale.getTime());
            maxQueryParam = Integer.parseInt(lastDayOfMonth);
            if(StringUtils.isNotEmpty(minQueryParam)){
                queryBuilder.appendAndHaving("t.statistic_time >= ?", minQueryParam);
            }
            if(StringUtils.isNotEmpty(maxQueryParam)){
                queryBuilder.appendAndHaving("t.statistic_time <= ?", maxQueryParam);
            }         
        }
        List<Map<String, Object>> list = baseDao.findListBySql(queryBuilder.getSql(), queryBuilder.getParams());
        if(list != null){
            for(Map<String, Object> map : list){
            	String date = StringUtils.nvlString(map.get("STATISTIC_TIME"));
            	int index = Integer.parseInt(date.substring(date.length()-2, date.length()));
            	yAxisDataA[index-1] =StringUtils.nvlInt(map.get("COUNTLEVELA"));
            	yAxisDataB[index-1] =StringUtils.nvlInt(map.get("COUNTLEVELB"));
            	yAxisDataC[index-1] =StringUtils.nvlInt(map.get("COUNTLEVELC"));
            	yAxisDataD[index-1] =StringUtils.nvlInt(map.get("COUNTLEVELD"));
            	yAxisDataE[index-1] = StringUtils.nvlInt(map.get("COUNTLEVELE"));
            }
        }
        yAxisData.add(yAxisDataA);
        yAxisData.add(yAxisDataB);
        yAxisData.add(yAxisDataC);
        yAxisData.add(yAxisDataD);
        yAxisData.add(yAxisDataE);	
        return yAxisData;
    }
    
    @Override
    public List<ImageSubstandardStatistics> getGatherQualitySubstandardCount(String dateStr,int xAxisCount,String departCode) throws Exception { 	
    	List<ImageSubstandardStatistics> yAxisDataList = new ArrayList<>();
    	List<ImageSubstandardStatistics> yAxisDataListTOPDEPART = new ArrayList<>();//分局数据

    	
    	String[] departCodeList = null ;
        if(StringUtils.isNotEmpty(departCode)){
            departCodeList = departCode.split(",");
        }else{
        	return yAxisDataList;
        }
        HashMap<String, Integer> departDataMap = new HashMap<String, Integer>();
        int listIndex = 0 ; //记录yAxisDataList集合中 部门的索引号，以便根据sql查询结果修改采集不合格率数组中数据
        for(int i=0;i<departCodeList.length;i++){
        	if(!departCodeList[i].equals(Constant.TOP_DEPARTCODE)){
            	ImageSubstandardStatistics  iss = new ImageSubstandardStatistics();
            	iss.setDepartCode(departCodeList[i]);
            	String departName = sysDepartService.findDepartNameByDepartCode(departCodeList[i]);
            	iss.setDepartName(departName);
            	int[] substandardPercent = new int[xAxisCount];
            	iss.setSubstandardPercent(substandardPercent);
        		departDataMap.put(departCodeList[i], listIndex++);
            	yAxisDataList.add(iss);
        	}else{//分局数据单独计算
        		yAxisDataListTOPDEPART = getGatherQualitySubstandardCountByTOPDepart(dateStr,xAxisCount);
        	}
        }
//        System.out.println(departCodeList.length);
    	//departCode = "";
        int minQueryParam;
        int maxQueryParam;

        StringBuilder sql = new StringBuilder();
        

        sql.append(" select t.depart_code AS departCode,")
        	.append("  t.depart_name AS departName,")
        	.append("ROUND(sum(t.SUBSTANDARD_COUNT)/sum(t.COUNT),2) AS substandardPercent,");

                
        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        
        if(xAxisCount==12){
        	queryBuilder.appendSql(" substr(t.statistic_time,0,6) statistic_time");
        	queryBuilder.appendSql(" from statistic_quality_day t");
        	if(StringUtils.isNotEmpty(departCodeList)){
        		queryBuilder.appendInOr(" t.depart_code ", departCodeList);
        	}
            queryBuilder.appendSql("group by substr(t.statistic_time,0,6),t.depart_code,t.depart_name");

            //设置年份查询条件
        	String year = dateStr.substring(0,4);
            minQueryParam = Integer.parseInt(year+"01");
            maxQueryParam = Integer.parseInt(year+"12");
            if(StringUtils.isNotEmpty(minQueryParam)){
                queryBuilder.appendAndHaving("substr(t.statistic_time,0,6) >= ?", minQueryParam);
            }
            if(StringUtils.isNotEmpty(maxQueryParam)){
                queryBuilder.appendAndHaving("substr(t.statistic_time,0,6) <= ?", maxQueryParam);
            }
        }else{
        	queryBuilder.appendSql("t.statistic_time statistic_time ");
        	queryBuilder.appendSql(" from statistic_quality_day t");
        	if(StringUtils.isNotEmpty(departCodeList)){
        		queryBuilder.appendInOr(" t.depart_code ", departCodeList);
        	}
            queryBuilder.appendSql("group by t.statistic_time,t.depart_code,t.depart_name");

        	//获取当月的第一天和最后一天
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
            Calendar cale = Calendar.getInstance(); 
            cale.set(Calendar.YEAR,Integer.parseInt(dateStr.substring(0,4)));
            cale.set(Calendar.MONTH,Integer.parseInt(dateStr.substring(5,dateStr.length()))-1);           
            int firstDay = cale.getActualMinimum(Calendar.DAY_OF_MONTH);
            int lastDay = cale.getActualMaximum(Calendar.DAY_OF_MONTH);
   			cale.set(Calendar.DAY_OF_MONTH, firstDay);
   			String firstDayOfMonth = sdf.format(cale.getTime());
            minQueryParam = Integer.parseInt(firstDayOfMonth);
   			cale.set(Calendar.DAY_OF_MONTH, lastDay);
   			String lastDayOfMonth = sdf.format(cale.getTime());
            maxQueryParam = Integer.parseInt(lastDayOfMonth);
            if(StringUtils.isNotEmpty(minQueryParam)){
                queryBuilder.appendAndHaving("t.statistic_time >= ?", minQueryParam);
            }
            if(StringUtils.isNotEmpty(maxQueryParam)){
                queryBuilder.appendAndHaving("t.statistic_time <= ?", maxQueryParam);
            }         
        }
        List<Map<String, Object>> list = baseDao.findListBySql(queryBuilder.getSql(), queryBuilder.getParams());
        
        ImageSubstandardStatistics iss = null;

        if(list.size()>0){
            for(Map<String, Object> map : list){
            	String resultDapartCode  = StringUtils.nvlString(map.get("DEPARTCODE"));
            	String departName  = StringUtils.nvlString(map.get("DEPARTNAME"));
            	String date = StringUtils.nvlString(map.get("STATISTIC_TIME"));
            	Integer yAxisDataIndex = departDataMap.get(resultDapartCode);
            	if(yAxisDataIndex!=null){
            		iss = yAxisDataList.get(yAxisDataIndex);
            		int[] substandardPercent = iss.getSubstandardPercent();
                	int index = Integer.parseInt(date.substring(date.length()-2, date.length()));
                	substandardPercent[index-1] =(int)(StringUtils.nvlDouble(map.get("SUBSTANDARDPERCENT"))*100);
                	iss.setSubstandardPercent(substandardPercent);
            	}
            }
        }
        if(yAxisDataListTOPDEPART.size()>0){//如果勾选了分局，则加入分局数据
        	yAxisDataList.addAll(yAxisDataListTOPDEPART);
        }
        return yAxisDataList;
    }
    @Override
    public List<ImageSubstandardStatistics> getGatherQualitySubstandardCountByTOPDepart(String dateStr,int xAxisCount) throws Exception {
    	List<ImageSubstandardStatistics> yAxisDataList = new ArrayList<>();
        int minQueryParam;
        int maxQueryParam;

        StringBuilder sql = new StringBuilder();
        

        sql.append(" select ")
        	.append("ROUND(sum(t.SUBSTANDARD_COUNT)/sum(t.COUNT),2) AS substandardPercent,");

                
        QueryBuilder queryBuilder = new QueryBuilder(sql.toString());
        
        if(xAxisCount==12){
        	queryBuilder.appendSql(" substr(t.statistic_time,0,6) statistic_time");
        	queryBuilder.appendSql(" from statistic_quality_day t");
            queryBuilder.appendSql("group by substr(t.statistic_time,0,6)");

            //设置年份查询条件
        	String year = dateStr.substring(0,4);
            minQueryParam = Integer.parseInt(year+"01");
            maxQueryParam = Integer.parseInt(year+"12");
            if(StringUtils.isNotEmpty(minQueryParam)){
                queryBuilder.appendAndHaving("substr(t.statistic_time,0,6) >= ?", minQueryParam);
            }
            if(StringUtils.isNotEmpty(maxQueryParam)){
                queryBuilder.appendAndHaving("substr(t.statistic_time,0,6) <= ?", maxQueryParam);
            }
        }else{
        	queryBuilder.appendSql("t.statistic_time statistic_time ");
        	queryBuilder.appendSql(" from statistic_quality_day t");
            queryBuilder.appendSql("group by t.statistic_time");

        	//获取当月的第一天和最后一天
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
            Calendar cale = Calendar.getInstance(); 
            cale.set(Calendar.YEAR,Integer.parseInt(dateStr.substring(0,4)));
            cale.set(Calendar.MONTH,Integer.parseInt(dateStr.substring(5,dateStr.length()))-1);           
            int firstDay = cale.getActualMinimum(Calendar.DAY_OF_MONTH);
            int lastDay = cale.getActualMaximum(Calendar.DAY_OF_MONTH);
   			cale.set(Calendar.DAY_OF_MONTH, firstDay);
   			String firstDayOfMonth = sdf.format(cale.getTime());
            minQueryParam = Integer.parseInt(firstDayOfMonth);
   			cale.set(Calendar.DAY_OF_MONTH, lastDay);
   			String lastDayOfMonth = sdf.format(cale.getTime());
            maxQueryParam = Integer.parseInt(lastDayOfMonth);
            if(StringUtils.isNotEmpty(minQueryParam)){
                queryBuilder.appendAndHaving("t.statistic_time >= ?", minQueryParam);
            }
            if(StringUtils.isNotEmpty(maxQueryParam)){
                queryBuilder.appendAndHaving("t.statistic_time <= ?", maxQueryParam);
            }         
        }
        List<Map<String, Object>> list = baseDao.findListBySql(queryBuilder.getSql(), queryBuilder.getParams());
        
        ImageSubstandardStatistics iss = null;
        HashMap<String, Integer> departDataMap = new HashMap<String, Integer>();
        int listIndex = 0 ;
        if(list != null){
            for(Map<String, Object> map : list){
            	String resultDapartCode  = Constant.TOP_DEPARTCODE;
            	String departName  = Constant.TOP_DEPARTNAME;
            	String date = StringUtils.nvlString(map.get("STATISTIC_TIME"));
            	Integer yAxisDataIndex = departDataMap.get(resultDapartCode);
            	if(yAxisDataIndex!=null){
            		iss = yAxisDataList.get(yAxisDataIndex);
            		int[] substandardPercent = iss.getSubstandardPercent();
                	int index = Integer.parseInt(date.substring(date.length()-2, date.length()));
                	substandardPercent[index-1] =(int)(StringUtils.nvlDouble(map.get("SUBSTANDARDPERCENT"))*100);
                	iss.setSubstandardPercent(substandardPercent);
            	}else{
            		departDataMap.put(resultDapartCode, listIndex++);
            		iss = new ImageSubstandardStatistics();
            		iss.setDepartCode(resultDapartCode);
            		iss.setDepartName(departName);
            		int[] substandardPercent = new int[xAxisCount];
                	int index = Integer.parseInt(date.substring(date.length()-2, date.length()));
                	substandardPercent[index-1] =(int)(StringUtils.nvlDouble(map.get("SUBSTANDARDPERCENT"))*100);
            		iss.setSubstandardPercent(substandardPercent);
            		yAxisDataList.add(iss);
            	}
            }
        }
        return yAxisDataList;
    }
    private int getIsCompelPass(char r, char p){
        return (r|p)-48;
    }
}
