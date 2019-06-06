package com.dao;

import com.entity.CodeArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CodeAreaDao extends CrudRepository<CodeArea,String> {

    /**
     * 查询行政区划列表
     * @param parentCode
     * @return
     */
    List<CodeArea> findByParentCodeOrderByCodeAsc(String parentCode);

    /**
     * 分页查询
     * @return
     */
    Page<CodeArea> findAll(Specification<CodeArea> params, Pageable pageable);

    /**
     * 通过code查询
     * @param code
     */
    CodeArea findByCode(String code);

}
