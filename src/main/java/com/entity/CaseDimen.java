package com.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
  * @author mengxin
  * @since 2019/5/20
  */
@Entity
@Table(name = "case_dimen")
public class CaseDimen {

  @Id
  @GenericGenerator(name="idGenerator", strategy="uuid")
  @GeneratedValue(generator="idGenerator")
  @Column(name = "id")
  private String id;
  @Column(name = "dimen_id")
  private String dimenId;
  @Column(name = "case_code")
  private String caseCode;
  @Column(name = "input_time")
  private Date inputTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDimenId() {
    return dimenId;
  }

  public void setDimenId(String dimenId) {
    this.dimenId = dimenId;
  }

  public String getCaseCode() {
    return caseCode;
  }

  public void setCaseCode(String caseCode) {
    this.caseCode = caseCode;
  }

  public Date getInputTime() {
    return inputTime;
  }

  public void setInputTime(Date inputTime) {
    this.inputTime = inputTime;
  }
}
