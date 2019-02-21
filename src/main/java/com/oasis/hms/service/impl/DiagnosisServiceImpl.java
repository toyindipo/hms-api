package com.oasis.hms.service.impl;

import com.oasis.hms.dao.DiagnosisDao;
import com.oasis.hms.model.Diagnosis;
import com.oasis.hms.service.DiagnosisService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Toyin on 2/7/19.
 */
@Service
public class DiagnosisServiceImpl implements DiagnosisService {
    private DiagnosisDao diagnosisDao;

    public DiagnosisServiceImpl(DiagnosisDao diagnosisDao) {
        this.diagnosisDao = diagnosisDao;
    }

    @Override
    public Diagnosis createDiagnosis(Diagnosis diagnosis) {
        return diagnosisDao.saveAndFlush(diagnosis);
    }

    @Override
    public Page<Diagnosis> findByCriteria(BooleanExpression expression, Pageable pageRequest) {
        return diagnosisDao.findAll(expression, pageRequest);
    }

    @Override
    public Diagnosis findByName(String name) {
        return diagnosisDao.findDiagnosisByNameEquals(name);
    }
}
