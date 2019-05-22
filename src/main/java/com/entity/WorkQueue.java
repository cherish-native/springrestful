package com.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 工作队列
 */
@Entity
@Table(name = "work_queue")
public class WorkQueue {

    /**
     * 唯一标识
     */
    private String id;
    /**
     * 插入时间
     */
    private Date insertTime;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 工作类型
     */
    private Integer workType;
    /**
     * 工作状态
     * 状态 0：未开始 1：后台服务正在执行 2：后台服务执行完成 3:正在统计 4:统计完成
     */
    private Integer workState;

    @Transient
    public static Integer TYPE_PERSON_LEVEL = 0;        //人员定级
    @Transient
    public static Integer TYPE_IMG_LEVEL = 1;           //人员等级设置
    @Transient
    public static Integer TYPE_IMG_LEVEL_SCORE = 2;     //图像质量等级范围设置

    @Transient
    public static Integer STATE_UNBEGIN = 0;
    @Transient
    public static Integer STATE_SERVICE_RUNNING = 1;
    @Transient
    public static Integer STATE_SERVICE_FINISH = 2;
    @Transient
    public static Integer STATE_STATISTIC_RUNNING = 3;
    @Transient
    public static Integer STATE_STATISTIC_FINISH = 4;

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "INSERT_TIME")
    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    @Column(name = "BEGIN_TIME")
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @Column(name = "END_TIME")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "WORK_TYPE")
    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    @Column(name = "WORK_STATE")
    public Integer getWorkState() {
        return workState;
    }

    public void setWorkState(Integer workState) {
        this.workState = workState;
    }
}
