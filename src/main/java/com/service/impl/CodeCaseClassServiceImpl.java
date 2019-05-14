package com.service.impl;

import com.dao.CodeCaseClassDao;
import com.service.CodeCaseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CodeCaseClassServiceImpl implements CodeCaseClassService {

    @Autowired
    private CodeCaseClassDao codeCaseClassDao;

}
