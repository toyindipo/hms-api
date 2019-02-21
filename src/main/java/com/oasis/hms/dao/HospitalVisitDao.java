package com.oasis.hms.dao;

import com.oasis.hms.model.HospitalVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Toyin on 2/1/19.
 */

@Repository
public interface HospitalVisitDao extends JpaRepository<HospitalVisit, Long>,
        QuerydslPredicateExecutor<HospitalVisit> {

    List<HospitalVisit> findByPatientIdAndCreatedAtBetween(Long id, LocalDateTime from, LocalDateTime to);

    Page<HospitalVisit> findByPatientId(Long id, Pageable pageable);

    Long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
