package com.service.impl;


import com.dao.*;
import com.entity.CaseDimen;
import com.entity.HuKouDimen;
import com.entity.PersonLevel;
import com.entity.PersonLevelScore;
import com.service.PersonLevelSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PersonLevelSetServiceImpl implements PersonLevelSetService {

    @Autowired
    private HuKouDimenDao huKouDimenDao;
    @Autowired
    private CaseDimenDao caseDimenDao;
    @Autowired
    private PersonLevelDao personLevelDao;
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private PersonLevelScoreDao personLevelScoreDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void personLevelSave(String[] addCodeArr,String[] caseCodeArr){
        String hukouDimenId = UUID.randomUUID().toString().replaceAll("-","");
        String caseDimenId = UUID.randomUUID().toString().replaceAll("-","");

        //修改原始配置的flag字段为0
        PersonLevel oldPersonLevel = personLevelDao.findByFlag(1);
        if(oldPersonLevel != null){
            oldPersonLevel.setFlag(0);
            personLevelDao.save(oldPersonLevel);
        }

        //保存新的人员定级配置
        PersonLevel personLevel = new PersonLevel();
        personLevel.setHukouDimenId(hukouDimenId);
        personLevel.setCaseDimenId(caseDimenId);
        personLevel.setInputTime(new Date());
        personLevel.setFlag(1);
        personLevelDao.save(personLevel);

        for(String addCode :addCodeArr){
            HuKouDimen huKouDimen = new HuKouDimen();
            huKouDimen.setDimenId(hukouDimenId);
            huKouDimen.setAddressCode(addCode);
            huKouDimen.setInputTime(new Date());
            huKouDimenDao.save(huKouDimen);
        }
        for(String caseCode : caseCodeArr){
            CaseDimen caseDimen = new CaseDimen();
            caseDimen.setDimenId(caseDimenId);
            caseDimen.setCaseCode(caseCode);
            caseDimen.setInputTime(new Date());
            caseDimenDao.save(caseDimen);
        }

    }

    @Override
    @Transactional
    public int updatePersonLevel() {
        int result = baseDao.updateBySql("update personinfo set person_level ='' ",null);
        return result;
    }

    @Override
    public Map<String, PersonLevelScore> getPersonLevelScoreSet() {
        Map<String, PersonLevelScore> map = new HashMap<>();
        map.put("1", personLevelScoreDao.findByLevel("A"));
        map.put("2", personLevelScoreDao.findByLevel("B"));
        map.put("3", personLevelScoreDao.findByLevel("C"));
        return map;
    }
}
