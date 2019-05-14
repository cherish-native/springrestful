package com.service;

import com.entity.CodeCaseClass;

import java.util.List;

/**
 * 字典-案件类别
 */
public interface CodeCaseClassService {

    /**
     * 查询案件类别列表
     * @param parentCode
     * @return
     */
    List<CodeCaseClass> listCodeCaseClass(String parentCode);

    /**
     * 模糊检索案件类别
     * @param searchStr
     * @return
     */
    List<CodeCaseClass> fuzzySearchCaseClassCode(String searchStr);

}
