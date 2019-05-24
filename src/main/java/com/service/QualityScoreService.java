package com.service;

import com.entity.QualityScoreRange;

import java.util.List;

public interface QualityScoreService {

    /**
     * 获取所有级别分数设置
     * @return
     */
    Iterable<QualityScoreRange> getAllQualityScoreRange();

}
