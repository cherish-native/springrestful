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
     * 强制通过总数
     */
    private Integer isCompelPassCount;
    /**
     * 非强制通过总数
     */
    private Integer unCompelPassCount;
    /**
     * 总数
     */
    private Integer count;
    /**
     * 平均分
     */
    private String scoreAverage;
    /**
     * 达标总数
     */
    private Integer standardCount;
    /**
     * 不达标总数
     */
    private Integer substandardCount;

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

    public void setIsCompelPassCount(Integer isCompelPassCount) {
        this.isCompelPassCount = isCompelPassCount;
    }

    @Column(name = "is_compel_pass_count")
    public Integer getIsCompelPassCount() {
        return isCompelPassCount;
    }

    public void setUnCompelPassCount(Integer unCompelPassCount) {
        this.unCompelPassCount = unCompelPassCount;
    }

    @Column(name = "un_compel_pass_count")
    public Integer getUnCompelPassCount() {
        return unCompelPassCount;
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
    @Column(name = "STANDARD_COUNT")
    public Integer getStandardCount() {
        return standardCount;
    }

    public void setStandardCount(Integer standardCount) {
        this.standardCount = standardCount;
    }
    @Column(name = "SUBSTANDARD_COUNT")
    public Integer getSubstandardCount() {
        return substandardCount;
    }

    public void setSubstandardCount(Integer substandardCount) {
        this.substandardCount = substandardCount;
    }
}