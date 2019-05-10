package com.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
  * @author mengxin
  * @since 2019/5/8
  */
@Entity
@Table(name = "person_level_score")
public class PersonLevelScore {

  @Id
  @GenericGenerator(name="idGenerator", strategy="uuid")
  @GeneratedValue(generator="idGenerator")
  @Column(name = "id")
  private String id;
  @Column(name = "level")
  private String level;
  @Column(name = "rm")
  private int rm;
  @Column(name = "rs")
  private int rs;
  @Column(name = "rz")
  private int rz;
  @Column(name = "rh")
  private int rh;
  @Column(name = "rx")
  private int rx;
  @Column(name = "lm")
  private int lm;
  @Column(name = "ls")
  private int ls;
  @Column(name = "lz")
  private int lz;
  @Column(name = "lh")
  private int lh;
  @Column(name = "lx")
  private int lx;
  @Column(name = "input_time")
  private Date inputTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public int getRm() {
    return rm;
  }

  public void setRm(int rm) {
    this.rm = rm;
  }

  public int getRs() {
    return rs;
  }

  public void setRs(int rs) {
    this.rs = rs;
  }

  public int getRz() {
    return rz;
  }

  public void setRz(int rz) {
    this.rz = rz;
  }

  public int getRh() {
    return rh;
  }

  public void setRh(int rh) {
    this.rh = rh;
  }

  public int getRx() {
    return rx;
  }

  public void setRx(int rx) {
    this.rx = rx;
  }

  public int getLm() {
    return lm;
  }

  public void setLm(int lm) {
    this.lm = lm;
  }

  public int getLs() {
    return ls;
  }

  public void setLs(int ls) {
    this.ls = ls;
  }

  public int getLz() {
    return lz;
  }

  public void setLz(int lz) {
    this.lz = lz;
  }

  public int getLh() {
    return lh;
  }

  public void setLh(int lh) {
    this.lh = lh;
  }

  public int getLx() {
    return lx;
  }

  public void setLx(int lx) {
    this.lx = lx;
  }

  public Date getInputTime() {
    return inputTime;
  }

  public void setInputTime(Date inputTime) {
    this.inputTime = inputTime;
  }
}
