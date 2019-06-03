package com.constant;

import com.entity.WorkQueue;

/**
 * 统计服务常量
 */
public class StatisticConstant {

    /**
     * 当前统计服务状态，在更改定级配置和质量评级配置时会更改此状态
     */
    public static Integer CURSTATE = WorkQueue.STATE_UNBEGIN;

    public final static Integer beginYear = 1990;

}
