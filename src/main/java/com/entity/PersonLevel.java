package com.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
  * @author mengxin
  * @since 2019/5/5
  */
@Entity
@Table(name = "person_level")
public class PersonLevel {

  @Id
  @GenericGenerator(name="idGenerator", strategy="uuid")
  @GeneratedValue(generator="idGenerator")
  @Column(name = "id")
  private String id;
  @Column(name = "hukou_dimen_id")
  private String hukouDimenId;
  @Column(name = "case_dimen_id")
  private String caseDimenId;
  @Column(name = "flag")
  private int flag;
  @Column(name = "input_time")
  private Date inputTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getHukouDimenId() {
    return hukouDimenId;
  }

  public void setHukouDimenId(String hukouDimenId) {
    this.hukouDimenId = hukouDimenId;
  }

  public String getCaseDimenId() {
    return caseDimenId;
  }

  public void setCaseDimenId(String caseDimenId) {
    this.caseDimenId = caseDimenId;
  }

  public int getFlag() {
    return flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  public Date getInputTime() {
    return inputTime;
  }

  public void setInputTime(Date inputTime) {
    this.inputTime = inputTime;
  }
}
