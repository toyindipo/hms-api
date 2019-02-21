package com.oasis.hms.service;

import com.oasis.hms.model.Diagnosis;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Toyin on 2/7/19.
 */
public interface DiagnosisService {
    Diagnosis createDiagnosis(Diagnosis diagnosis);

    Page<Diagnosis> findByCriteria(BooleanExpression expression, Pageable pageRequest);

    Diagnosis findByName(String name);
}
