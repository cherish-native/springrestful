package com.dao;

import com.entity.SysDepart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

public interface SysDepartDao  extends CrudRepository<SysDepart,String> {

    /**
     * 分页查询
     * @return
     */
    Page<SysDepart> findAll(Specification<SysDepart> params, Pageable pageable);

}
