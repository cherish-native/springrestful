package com.service;

import com.entity.SysDepart;

import java.util.List;

/**
 * 部门service
 */
public interface SysDepartService {

    /**
     * 部门模糊检索
     * @param fuzzyStr
     * @return
     */
    List<SysDepart> fuzzySearchDepart(String fuzzyStr);

}
