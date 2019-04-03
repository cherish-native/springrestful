package com.controller.statistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 数据统计
 */
@Controller
@RequestMapping(value = "/pages/statistics")
public class StatisticsController {


    /**
     * 历史图像质量统计
     * @param departCode 部门编码
     * @param dateStr 统计时间段，分为 yyyy 和 yyyy-MM
     * @return
     */
    @RequestMapping("/historyImageQualityStatistics")
    @ResponseBody
    public Map<String,Object> historyImageQualityStatistics(String departCode,String dateStr) throws Exception {
        Map<String,Object> result = new HashMap<>();
        //横坐标数据
        int xAxisCount = 12;
        String xAxisSuffix = "月";
        if(dateStr.length()==7){
            xAxisCount = getDayCountOfMonth(dateStr);
            xAxisSuffix = "";
        }
        String[] xAxisData = new String[xAxisCount];
        for(int i=0;i<xAxisCount;i++){
            xAxisData[i] = (i+1)+xAxisSuffix;
        }
        //纵坐标数据，模拟数据
        List<String[]> yAxisData = new ArrayList<>();
        for(int i=0;i<5;i++){
            String[] yAxisDataItem = new String[xAxisCount];
            for(int j=0;j<yAxisDataItem.length;j++){
                Random rand = new Random();
                yAxisDataItem[j] = rand.nextInt(100)+1+"";
            }
            yAxisData.add(yAxisDataItem);
        }
        result.put("xAxisData", xAxisData);
        result.put("series", yAxisData);
        return result;
    }


    /**
     * 历史未达标数据统计
     * @param departCode
     * @param dateStr
     * @return
     * @throws Exception
     */
    @RequestMapping("/historyImageSubstandardStatistics")
    @ResponseBody
    public Map<String,Object> historyImageSubstandardStatistics(String departCode,String dateStr) throws Exception{
        Map<String,Object> result = new HashMap<>();
        //横坐标数据
        int xAxisCount = 12;
        String xAxisSuffix = "月";
        if(dateStr.length()==7){
            xAxisCount = getDayCountOfMonth(dateStr);
            xAxisSuffix = "";
        }
        String[] xAxisData = new String[xAxisCount];
        for(int i=0;i<xAxisCount;i++){
            xAxisData[i] = (i+1)+xAxisSuffix;
        }

        String[] yAxisData = new String[xAxisCount];
        for(int i=0;i<yAxisData.length;i++){
            Random rand = new Random();
            yAxisData[i] = rand.nextInt(99)+1+".5";
        }
        result.put("xAxisData", xAxisData);
        result.put("yAxisData", yAxisData);
        return result;
    }



    /**
     * 获取每月总天数
     * @param dataStr
     * @return
     * @throws Exception
     */
    private int getDayCountOfMonth(String dataStr) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = sdf.parse(dataStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
