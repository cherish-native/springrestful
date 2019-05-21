package com.controller.statistics;

import com.constant.StatisticConstant;
import com.controller.BaseController;
import com.entity.StatisticQualityDay;
import com.entity.WorkQueue;
import com.entity.vo.DataGridReturn;
import com.service.StatisticService;
import com.service.WorkQueueService;
import com.sun.deploy.net.protocol.chrome.ChromeURLConnection;
import com.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据统计
 */
@Controller
@RequestMapping(value = "/pages/statistics")
public class StatisticsController extends BaseController {

    private static Logger logger = Logger.getLogger(StatisticsController.class);

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private StatisticService statisticService;
    @Autowired
    private WorkQueueService workQueueService;

    /**
     * 获取每日图像质量统计（首页地图展示）
     * @return
     */
    @RequestMapping("/imageQualityStatisticsDay")
    @ResponseBody
    public Map<String,Object> getImageQualityStatisticsDay(){
        int date = Integer.parseInt(DateUtil.getDateStr(new Date(), DateUtil.PATTERN_YYYYMMDD));
        Map<String, Object> result = statisticService.getImageQualityStatisticsDay(date);
        return result;
    }

    /**
     * 采集质量考核列表
     * @return
     */
    @RequestMapping("/gatherQualityExamineList")
    @ResponseBody
    public DataGridReturn gatherQualityExamineList(String departCode, String beginDate, String endDate, HttpServletRequest request) throws Exception {
        return statisticService.getGatherQualityExamineList(departCode, beginDate, endDate, getPagination(request));
    }

    /**
     * 采集未达标列表
     * @param departCode
     * @param beginDate
     * @param endDate
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/gatherSubstandardExamineList")
    public DataGridReturn gatherSubstandardExamineList(String departCode, String beginDate, String endDate, HttpServletRequest request){
        return statisticService.gatherSubstandardExamineList(departCode, beginDate, endDate, getPagination(request));
    }

    /**
     * 采集未达标详细列表
     * @param departCode
     * @param beginDate
     * @param endDate
     * @param request
     * @return
     */
    public DataGridReturn gatherSubstandardExamineDetailList(String departCode, String beginDate, String endDate, HttpServletRequest request){
        return statisticService.gatherSubstandardExamineDetailList(departCode, beginDate, endDate, getPagination(request));
    }

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

    @ResponseBody
    @RequestMapping("/changeWorkQueueState/{state}")
    public String changeWorkQueueState(@PathVariable("state") Integer state) throws Exception{
        String result = "success";
        System.out.println(state);
        StatisticConstant.CURSTATE = state;
        if(StatisticConstant.CURSTATE == WorkQueue.STATE_SERVICE_FINISH){
            //后台服务执行完毕，开始执行统计
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //开始执行统计服务
                    try {
                        Calendar calendar = Calendar.getInstance();
                        int lastYear = calendar.get(Calendar.YEAR);
                        int lastDay = Integer.parseInt(DateUtil.getDateStr(new Date(), DateUtil.PATTERN_YYYYMMDD));
                        logger.info("开始统计");
                        for(int year=1990;year<=lastYear;year++){
                            for(int month=1;month<=12;month++){
                                int dayCount = getDayCountOfMonth(year+"-"+month);
                                for(int day=1;day<=dayCount;day++){
                                    int curDay = Integer.parseInt(year+""+String.format("%02d", month)+String.format("%02d", day));
                                    if(curDay<=lastDay){
                                        statisticService.statisticDay(curDay+"");
                                        logger.info("正在统计：" + curDay);
                                    }else {
                                        break;
                                    }
                                }
                            }
                        }
                        logger.info("统计完毕");
                    } catch (Exception e){
                        e.printStackTrace();
                        logger.error("统计错误", e);
                    }
                }
            });
        }
        return result;
    }
}
