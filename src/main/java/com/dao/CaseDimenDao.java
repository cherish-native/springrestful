package com.dao;

import com.entity.CaseDimen;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @since mengxin
 * 2019/5/20
 */
public interface CaseDimenDao extends CrudRepository<CaseDimen,Integer> {

    List<CaseDimen> findByDimenId(String dimenId);
}
