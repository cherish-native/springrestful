package com.service.impl;

import com.dao.QualityScoreRangeDao;
import com.entity.QualityScoreRange;
import com.service.QualityScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class QualityScoreServiceImpl implements QualityScoreService {

    @Autowired
    private QualityScoreRangeDao qualityScoreRangeDao;

    @Override
    public Iterable<QualityScoreRange> getAllQualityScoreRange() {
        return qualityScoreRangeDao.findAll();
    }
}
