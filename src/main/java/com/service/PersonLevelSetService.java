package com.service;

import com.entity.CodeArea;

import java.util.List;

/**
 *
 */
public interface PersonLevelSetService {

    void personLevelSave(String[] addCodeArr,String[] caseCodeArr);

    int updatePersonLevel();
}
