package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 行政区划编码
 */
@Entity
@Table(name = "code_area")
public class CodeArea {

    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 简称
     */
    private String singleName;
    /**
     * 名称拼音简写
     */
    private String pinyinInititals;
    /**
     * 上级编码
     */
    private String parentCode;
    /**
     * 级别（1:省/直辖市，2:市，3:区/乡镇）
     */
    private Integer level;

    @Id
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "single_name")
    public String getSingleName() {
        return singleName;
    }

    public void setSingleName(String singleName) {
        this.singleName = singleName;
    }

    @Column(name = "pinyin_inititals")
    public String getPinyinInititals() {
        return pinyinInititals;
    }

    public void setPinyinInititals(String pinyinInititals) {
        this.pinyinInititals = pinyinInititals;
    }

    @Column(name = "parent_code")
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Column(name = "level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
