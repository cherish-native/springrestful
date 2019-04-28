package com.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "statistic_quality_day")
public class StatisticQualityDay {

    /**
     * 唯一标识
     */
    private String id;
    /**
     * 部门编号
     */
    private String departCode;
    /**
     * 部门名称
     */
    private String departName;
    /**
     * 采集人姓名
     */
    private String gatheruserName;
    /**
     * 采集人ID
     */
    private String gatheruserId;
    /**
     * 统计时间,内容为年月日yyyyMMdd
     */
    private Integer statisticTime;
    /**
     * A级总数
     */
    private Integer countLevelA;
    /**
     * B级总数
     */
    private Integer countLevelB;
    /**
     * C级总数
     */
    private Integer countLevelC;
    /**
     * D级总数
     */
    private Integer countLevelD;
    /**
     * E级总数
     */
    private Integer countLevelE;
    /**
     * 是否强制通过 0:否 1:是
     */
    private Integer isCompelPass;
    /**
     * 总数
     */
    private Integer count;
    /**
     * 平均分
     */
    private String scoreAverage;

    public void setId(String id) {
        this.id = id;
    }

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

    @Column(name = "depart_code")
    public String getDepartCode() {
        return departCode;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    @Column(name = "depart_name")
    public String getDepartName() {
        return departName;
    }

    public void setGatheruserName(String gatheruserName) {
        this.gatheruserName = gatheruserName;
    }

    @Column(name = "gatheruser_name")
    public String getGatheruserName() {
        return gatheruserName;
    }

    public void setGatheruserId(String gatheruserId) {
        this.gatheruserId = gatheruserId;
    }

    @Column(name = "gatheruser_id")
    public String getGatheruserId() {
        return gatheruserId;
    }

    public void setStatisticTime(Integer statisticTime) {
        this.statisticTime = statisticTime;
    }

    @Column(name = "statistic_time")
    public Integer getStatisticTime() {
        return statisticTime;
    }

    public void setCountLevelA(Integer countLevelA) {
        this.countLevelA = countLevelA;
    }

    @Column(name = "count_level_a")
    public Integer getCountLevelA() {
        return countLevelA;
    }

    public void setCountLevelB(Integer countLevelB) {
        this.countLevelB = countLevelB;
    }

    @Column(name = "count_level_b")
    public Integer getCountLevelB() {
        return countLevelB;
    }

    public void setCountLevelC(Integer countLevelC) {
        this.countLevelC = countLevelC;
    }

    @Column(name = "count_level_c")
    public Integer getCountLevelC() {
        return countLevelC;
    }

    public void setCountLevelD(Integer countLevelD) {
        this.countLevelD = countLevelD;
    }

    @Column(name = "count_level_d")
    public Integer getCountLevelD() {
        return countLevelD;
    }

    public void setCountLevelE(Integer countLevelE) {
        this.countLevelE = countLevelE;
    }

    @Column(name = "count_level_e")
    public Integer getCountLevelE() {
        return countLevelE;
    }

    public void setIsCompelPass(Integer isCompelPass) {
        this.isCompelPass = isCompelPass;
    }

    @Column(name = "is_compel_pass")
    public Integer getIsCompelPass() {
        return isCompelPass;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Column(name = "count")
    public Integer getCount() {
        return count;
    }

    public void setScoreAverage(String scoreAverage) {
        this.scoreAverage = scoreAverage;
    }

    @Column(name = "score_average")
    public String getScoreAverage() {
        return scoreAverage;
    }
}