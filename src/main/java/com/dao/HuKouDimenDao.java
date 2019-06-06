package com.dao;

import com.entity.HuKouDimen;
import com.entity.PersonLevelScore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @since mengxin
 * 2019/5/20
 */
public interface HuKouDimenDao extends CrudRepository<HuKouDimen,Integer> {

    List<HuKouDimen> findByDimenId(String dimenId);

}
