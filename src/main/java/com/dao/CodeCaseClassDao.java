package com.dao;

import com.entity.CodeCaseClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CodeCaseClassDao extends CrudRepository<CodeCaseClass,String> {

    /**
     * 查询案件类别编码
     * @param parentCode
     * @return
     */
    List<CodeCaseClass> findByParentCodeOrderByCodeAsc(String parentCode);

    /**
     * 分页查询
     * @return
     */
    Page<CodeCaseClass> findAll(Specification<CodeCaseClass> params, Pageable pageable);

}
