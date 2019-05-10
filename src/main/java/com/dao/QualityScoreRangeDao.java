package com.dao;

import com.entity.QualityScoreRange;
import com.entity.SysUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @since 2019/5/7
 */
public interface QualityScoreRangeDao extends CrudRepository<QualityScoreRange,Integer> {

    QualityScoreRange findByLevel(int level);
}
