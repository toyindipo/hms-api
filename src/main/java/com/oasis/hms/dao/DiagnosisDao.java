package com.oasis.hms.dao;

import com.oasis.hms.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Toyin on 2/1/19.
 */
@Repository
public interface DiagnosisDao extends JpaRepository<Diagnosis, Long>,
        QuerydslPredicateExecutor<Diagnosis> {
    Diagnosis findDiagnosisByNameEquals(String name);
}
