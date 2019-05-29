package com.service;

import com.entity.CodeArea;
import com.entity.PersonLevelScore;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface PersonLevelSetService {

    void personLevelSave(String[] addCodeArr,String[] caseCodeArr);

    int updatePersonLevel();

    /**
     * 获取人员定级分数设置
     * @return
     */
    Map<String, PersonLevelScore> getPersonLevelScoreSet();
}
