package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 案件类别编码
 */
@Entity
@Table(name = "code_caseclass")
public class CodeCaseClass {

    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 名称拼音简写
     */
    private String pinyinInititals;
    /**
     * 上级编码
     */
    private String parentCode;

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

}
