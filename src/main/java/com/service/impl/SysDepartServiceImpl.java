package com.service.impl;

import com.dao.BaseDao;
import com.dao.SysDepartDao;
import com.entity.SysDepart;
import com.service.SysDepartService;
import com.util.QueryBuilder;
import com.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class SysDepartServiceImpl implements SysDepartService {

    @Autowired
    private SysDepartDao sysDepartDao;
    @Autowired
    private BaseDao baseDao;

    @Override
    public List<SysDepart> fuzzySearchDepart(String fuzzyStr) {
        List<SysDepart> sysDepartList = new ArrayList<>();
        String sql = "select id, depart_code departCode, depart_name departName, pinyin_inititals pinyinInititals from sys_depart ";
        QueryBuilder queryBuilder = new QueryBuilder(sql);
        if(StringUtils.isNotEmpty(fuzzyStr)){
            fuzzyStr = fuzzyStr + "%";
            queryBuilder.appendAndWhere("(depart_code like ? or depart_name like ? or pinyin_inititals like ?)", fuzzyStr, fuzzyStr, fuzzyStr);
        }
        queryBuilder.appendSql("order by depart_code");
        List<Map<String, Object>> list = baseDao.findListBySql(queryBuilder.getSql(), queryBuilder.getParams());
        if(list != null && list.size() > 0){
            for(Map<String, Object> map : list){
                SysDepart sysDepart = new SysDepart();
                sysDepart.setId(StringUtils.nvlString(map.get("id")));
                sysDepart.setDepartCode(StringUtils.nvlString(map.get("departCode")));
                sysDepart.setDepartName(StringUtils.nvlString(map.get("departName")));
                sysDepart.setPinyinInititals(StringUtils.nvlString(map.get("pinyinInititals")));
                sysDepartList.add(sysDepart);
            }
        }
        return sysDepartList;
    }
}
