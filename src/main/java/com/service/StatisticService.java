package com.service;

import com.entity.StatisticQualityDay;
import com.entity.vo.DataGridReturn;
import com.entity.vo.Pagination;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface StatisticService {

    /**
     * 获取每日图像质量统计结果
     * @param date
     * @return
     */
    Map<String, Object> getImageQualityStatisticsDay(int date);

    /**
     * 获取采集质量考核列表
     * @param departCode
     * @param beginDate
     * @param endDate
     * @param pageable
     * @return
     */
    DataGridReturn getGatherQualityExamineList(String departCode, String beginDate, String endDate, Pageable pageable) throws Exception;

    /**
     * 统计每日数据
     * @param dateStr yyyyMMdd
     */
    void statisticDay(String dateStr);

    /**
     * 采集未达标列表
     * @param departCode
     * @param beginDate
     * @param endDate
     * @param pagination
     * @return
     */
    DataGridReturn gatherSubstandardExamineList(String departCode, String beginDate, String endDate, Pageable pagination);

    /**
     * 采集未达标详细列表
     * @param departCode
     * @param beginDate
     * @param endDate
     * @param pagination
     * @return
     */
    DataGridReturn gatherSubstandardExamineDetailList(String departCode, String gatherUser, String beginDate, String endDate, Pageable pagination);

    /**
     * 获取强制通过统计列表
     * @param departCode
     * @param beginDate
     * @param endDate
     * @param pagination
     * @return
     */
    DataGridReturn gatherCompelPassList(String departCode, String beginDate, String endDate, Pageable pagination);

    /**
     * 强制通过详细列表
     * @param departCode
     * @param gatheruserName
     * @param beginDate
     * @param endDate
     * @param pagination
     * @return
     */
    DataGridReturn gatherSubstandardCompelPassDetailList(String departCode, String gatheruserName, String beginDate, String endDate, Pageable pagination);
    /**
     * 统计采集质量
     * @param dateStr
     * @param xAxisCount
     * @return
     * @throws Exception
     */
    List<String[]> getGatherQualityCount(String dateStr,int xAxisCount,String departCode) throws Exception;
    /**
     * 历史采集未达标统计
     * @param dateStr
     * @param xAxisCount
     * @return
     * @throws Exception
     */
    List<String[]> getGatherQualitySubstandardCount(String dateStr,int xAxisCount,String departCode) throws Exception;

}
