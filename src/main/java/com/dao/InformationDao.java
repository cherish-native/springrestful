package com.dao;

import com.entity.Information;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InformationDao extends CrudRepository<Information,Integer> {

    List<Information> findByIsStart(int isStart);

    Information findById(String id);

    Information findByCode(String code);

    @Transactional
    void deleteInformationById(String id);
}