package com.service.impl;

import com.dao.BaseDao;
import com.dao.CodeAreaDao;
import com.dao.StatisticQualityDayDao;
import com.entity.CodeArea;
import com.entity.StatisticQualityDay;
import com.service.CodeAreaService;
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

@Service
@Transactional(readOnly = true)
public class CodeAreaServiceImpl implements CodeAreaService {

    @Autowired
    private CodeAreaDao codeAreaDao;
    @Autowired
    private StatisticQualityDayDao statisticQualityDayDao;

    @Override
    public List<CodeArea> listCodeArea(String parentCode) {
        if(parentCode == null){
            parentCode = "";
        }
        return codeAreaDao.findByParentCodeOrderByCodeAsc(parentCode);
    }

    @Override
    public List<CodeArea> fuzzySearchCodeArea(String searchStr) {
        //默认查询30条
        Pageable pageable = new PageRequest(0, 30);
        Specification<CodeArea> params = new Specification<CodeArea>() {
            @Override
            public Predicate toPredicate(Root<CodeArea> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                  List<Predicate> predicates = new ArrayList<>();
                  if(StringUtils.isNotEmpty(searchStr)){
                      predicates.add(criteriaBuilder.like(root.get("code"), searchStr + "%"));
                      predicates.add(criteriaBuilder.like(root.get("name"), searchStr + "%"));
                      predicates.add(criteriaBuilder.like(root.get("pinyinInititals"), searchStr.toUpperCase() + "%"));
                  }
                Predicate[] predicatesArray = predicates.toArray (new Predicate[predicates.size()]);
                criteriaQuery.where(criteriaBuilder.or(predicatesArray));
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("code")));
                return criteriaQuery.getRestriction();
            }
        };
        Page<CodeArea> page = codeAreaDao.findAll(params, pageable);
        return page.getContent();
    }
}
