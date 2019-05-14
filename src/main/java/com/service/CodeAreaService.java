package com.service;

import com.entity.CodeArea;

import java.util.List;

/**
 * 字典-行政区划
 */
public interface CodeAreaService {

    List<CodeArea> listCodeArea(String parentCode);

    /**
     * 刑侦区划代码模糊检索
     * @param searchStr
     * @return
     */
    List<CodeArea> fuzzySearchCodeArea(String searchStr);
}
