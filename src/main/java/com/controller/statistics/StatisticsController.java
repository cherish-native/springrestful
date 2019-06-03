package com.controller.statistics;

import com.constant.StatisticConstant;
import com.controller.BaseController;
import com.entity.StatisticQualityDay;
import com.entity.WorkQueue;
import com.entity.vo.DataGridReturn;
import com.entity.vo.ImageSubstandardStatistics;
import com.service.StatisticService;
import com.service.WorkQueueService;
import com.util.DateUtil;
import com.util.FTPUtil;
import com.util.FileUtil;
import com.util.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @RequestMapping("/gatherSubstandardExamineDetailList")
    @ResponseBody
    public DataGridReturn gatherSubstandardExamineDetailList(String departCode, String gatheruserName, String beginDate, String endDate, HttpServletRequest request){
        return statisticService.gatherSubstandardExamineDetailList(departCode, gatheruserName, beginDate, endDate, getPagination(request));
    }

    /**
     * 获取强制通过统计列表
     * @param departCode
     * @param beginDate
     * @param endDate
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/gatherCompelPassList")
    public DataGridReturn gatherCompelPassList(String departCode, String beginDate, String endDate, HttpServletRequest request){
        return statisticService.gatherCompelPassList(departCode, beginDate, endDate, getPagination(request));
    }

    /**
     * 强制通过详细列表
     * @param departCode
     * @param gatheruserName
     * @param beginDate
     * @param endDate
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/gatherSubstandardCompelPassDetailList")
    public DataGridReturn gatherSubstandardCompelPassDetailList(String departCode, String gatheruserName, String beginDate, String endDate, HttpServletRequest request){
        return statisticService.gatherSubstandardCompelPassDetailList(departCode, gatheruserName, beginDate, endDate, getPagination(request));
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
        List<int[]> yAxisData = new ArrayList<>();
//        for(int i=0;i<5;i++){
//            String[] yAxisDataItem = new String[xAxisCount];
//            yAxisData.add(yAxisDataItem);
//        }
    	yAxisData = statisticService.getGatherQualityCount(dateStr,xAxisCount,departCode);
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
        List<String[]> yAxisData = new ArrayList<>();
        	for(int a=0; a<3;a++){
            	String[] yAxisDataItem = new String[xAxisCount];
	        	 for(int i=0;i<xAxisCount;i++){
	                 Random rand = new Random();
	                 yAxisDataItem[i] = rand.nextInt(99)+1+".5";
	        	 }
                 yAxisData.add(yAxisDataItem);

        	}
        List<ImageSubstandardStatistics> y = new ArrayList<ImageSubstandardStatistics>();
        	
    	y = statisticService.getGatherQualitySubstandardCount(dateStr,xAxisCount,departCode);

        result.put("xAxisData", xAxisData);
        result.put("yAxisData", y);
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
        WorkQueue currentWorkQueue = workQueueService.getLastWorkQueue();
        if(currentWorkQueue != null && currentWorkQueue.getWorkState() != WorkQueue.STATE_STATISTIC_FINISH){
            StatisticConstant.CURSTATE = state;
            currentWorkQueue.setWorkState(state);
            workQueueService.save(currentWorkQueue);
            logger.info("修改状态，ID:" + currentWorkQueue.getId() + "curSate:"+ currentWorkQueue.getWorkState() +",state:" + state);
            if(state == WorkQueue.STATE_SERVICE_FINISH){
                if(StatisticConstant.CURSTATE == state){
                    //后台服务执行完毕，开始执行统计
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            //开始执行统计服务
                            try {
                                StatisticConstant.CURSTATE = WorkQueue.STATE_STATISTIC_RUNNING;
                                Calendar calendar = Calendar.getInstance();
                                int lastYear = calendar.get(Calendar.YEAR);
                                int lastDay = Integer.parseInt(DateUtil.getDateStr(new Date(), DateUtil.PATTERN_YYYYMMDD));
                                logger.info("开始统计");
                                currentWorkQueue.setWorkState(WorkQueue.STATE_STATISTIC_RUNNING);
                                workQueueService.save(currentWorkQueue);
                                for(int year=StatisticConstant.beginYear;year<=lastYear;year++){
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
                                currentWorkQueue.setWorkState(WorkQueue.STATE_STATISTIC_FINISH);
                                currentWorkQueue.setEndTime(new Date());
                                workQueueService.save(currentWorkQueue);
                                StatisticConstant.CURSTATE = WorkQueue.STATE_STATISTIC_FINISH;
                            } catch (Exception e){
                                e.printStackTrace();
                                logger.error("统计错误", e);
                            }
                        }
                    });
                }
            }
        }else{
            result = "change workstate fail";
        }
        return result;
    }

    /**
     * 获取指纹图像
     * @param imgPath 图像文件夹路径
     * @param personId 捺印卡号
     * @param fgpCase 0：滚动 1：平面
     * @param fgp 指纹 1-10
     * @param type 图像类型 1：指纹原图  2：红白图
     * @param response
     */
    @RequestMapping("/showFingerImage/{imgPath}/{personId}/{fgpCase}/{fgp}/{type}")
    @ResponseBody
    public void showFingerImage(@PathVariable("imgPath") String imgPath, @PathVariable("personId") String personId, @PathVariable("fgpCase") int fgpCase, @PathVariable("fgp") int fgp,
                                @PathVariable("type") int type, HttpServletResponse response) throws Exception{
        byte[] imageBytes = null;
        int baseInt = fgpCase*10;
        try {
            if(type == 1){
                imageBytes = FTPUtil.downFile(imgPath,personId + "_" + (baseInt+fgp) + ".bmp");
            }else{
                imageBytes = FTPUtil.downFile(imgPath,personId + "_" + (baseInt+fgp) + ".jpg");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if(imageBytes == null){
            imageBytes = new byte[0];
        }
        response.getOutputStream().write(imageBytes);
    }
}
