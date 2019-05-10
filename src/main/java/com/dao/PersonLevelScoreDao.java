package com.dao;

import com.entity.PersonLevelScore;
import org.springframework.data.repository.CrudRepository;

/**
 * @since 2019/5/7
 */
public interface PersonLevelScoreDao extends CrudRepository<PersonLevelScore,Integer> {

    PersonLevelScore findByLevel(String level);
}
