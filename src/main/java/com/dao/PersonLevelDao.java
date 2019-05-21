package com.dao;

import com.entity.PersonLevel;
import org.springframework.data.repository.CrudRepository;

/**
 * @since mengxin
 * 2019/5/20
 */
public interface PersonLevelDao extends CrudRepository<PersonLevel,Integer> {

    PersonLevel findByFlag(int flag);

}
