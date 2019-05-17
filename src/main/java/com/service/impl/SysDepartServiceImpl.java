package com.service.impl;

import com.dao.BaseDao;
import com.dao.SysDepartDao;
import com.entity.CodeArea;
import com.entity.SysDepart;
import com.service.SysDepartService;
import com.util.QueryBuilder;
import com.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
//        List<SysDepart> sysDepartList = new ArrayList<>();
//        String sql = "select id, depart_code departCode, depart_name departName, pinyin_inititals pinyinInititals from sys_depart ";
//        QueryBuilder queryBuilder = new QueryBuilder(sql);
//        if(StringUtils.isNotEmpty(fuzzyStr)){
//            fuzzyStr = fuzzyStr + "%";
//            queryBuilder.appendAndWhere("(depart_code like ? or depart_name like ? or pinyin_inititals like ?)", fuzzyStr, fuzzyStr, fuzzyStr);
//        }
//        queryBuilder.appendSql("order by depart_code");
//        List<Map<String, Object>> list = baseDao.findListBySql(queryBuilder.getSql(), queryBuilder.getParams());
//        if(list != null && list.size() > 0){
//            for(Map<String, Object> map : list){
//                SysDepart sysDepart = new SysDepart();
//                sysDepart.setId(StringUtils.nvlString(map.get("id")));
//                sysDepart.setDepartCode(StringUtils.nvlString(map.get("departCode")));
//                sysDepart.setDepartName(StringUtils.nvlString(map.get("departName")));
//                sysDepart.setPinyinInititals(StringUtils.nvlString(map.get("pinyinInititals")));
//                sysDepartList.add(sysDepart);
//            }
//        }
//        return sysDepartList;

        //默认查询30条
        Pageable pageable = new PageRequest(0, 30);
        Specification<SysDepart> params = new Specification<SysDepart>() {
            @Override
            public Predicate toPredicate(Root<SysDepart> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotEmpty(fuzzyStr)){
                    predicates.add(criteriaBuilder.like(root.get("departCode"), fuzzyStr + "%"));
                    predicates.add(criteriaBuilder.like(root.get("departName"), fuzzyStr + "%"));
                    predicates.add(criteriaBuilder.like(root.get("pinyinInititals"), fuzzyStr.toUpperCase() + "%"));
                }
                if(predicates.size() > 0){
                    Predicate[] predicatesArray = predicates.toArray (new Predicate[predicates.size()]);
                    criteriaQuery.where(criteriaBuilder.or(predicatesArray));
                }
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("departCode")));
                return criteriaQuery.getRestriction();
            }
        };
        Page<SysDepart> page = sysDepartDao.findAll(params, pageable);
        return page.getContent();
    }
}
