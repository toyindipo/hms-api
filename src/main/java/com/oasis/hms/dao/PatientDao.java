package com.oasis.hms.dao;

import com.oasis.hms.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Created by Toyin on 2/1/19.
 */
@Repository
public interface PatientDao extends JpaRepository<Patient, Long>,
        QuerydslPredicateExecutor<Patient> {
    Patient findByHospitalNumber(String hospitalNumber);

    Long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
