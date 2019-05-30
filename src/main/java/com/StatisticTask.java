package com;

import com.service.StatisticService;
import com.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StatisticTask {

    private static Logger logger = Logger.getLogger(StatisticTask.class);

    @Autowired
    private StatisticService statisticService;

    @Scheduled(cron = "0 0/3 * * * ?")
    public void dailyStastic(){
        logger.info("每日统计begin");
        Date date = new Date();
        String dateStr = DateUtil.getDateStr(date, DateUtil.PATTERN_YYYYMMDD);
        statisticService.statisticDay(dateStr);
        logger.info("每日统计end");
    }

}
