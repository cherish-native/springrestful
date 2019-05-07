package com.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "sys_depart")
public class SysDepart {

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
     * 拼音首字母
     */
    private String pinyinInititals;

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

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "pinyin_inititals")
    public String getPinyinInititals() {
        return pinyinInititals;
    }

    public void setPinyinInititals(String pinyinInititals) {
        this.pinyinInititals = pinyinInititals;
    }
}
