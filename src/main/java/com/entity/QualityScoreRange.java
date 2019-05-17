package com.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
  * @author mengxin
  * @since 2019/5/5
  */
@Entity
@Table(name = "quality_score_range")
public class QualityScoreRange{

  @Id
  @GenericGenerator(name="idGenerator", strategy="uuid")
  @GeneratedValue(generator="idGenerator")
  @Column(name = "id")
  private String id;
  @Column(name = "min_score")
  private int minScore;
  @Column(name = "max_score")
  private int maxScore;
  @Column(name = "score_level")
  private int level;
  @Column(name = "name")
  private String name;
  @Column(name = "input_time")
  private Date inputTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getMinScore() {
    return minScore;
  }

  public void setMinScore(int minScore) {
    this.minScore = minScore;
  }

  public int getMaxScore() {
    return maxScore;
  }

  public void setMaxScore(int maxScore) {
    this.maxScore = maxScore;
  }

  public Date getInputTime() {
    return inputTime;
  }

  public void setInputTime(Date inputTime) {
    this.inputTime = inputTime;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
