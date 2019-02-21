package com.oasis.hms.service;

import com.oasis.hms.dto.StatDto;
import com.oasis.hms.model.HospitalVisit;
import com.oasis.hms.model.Patient;
import com.oasis.hms.model.User;
import com.oasis.hms.model.enums.VisitState;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Toyin on 2/2/19.
 */
public interface HospitalVisitService {
    HospitalVisit createVisit(Patient patient, User deskOfficer);

    HospitalVisit assignToDoctor(HospitalVisit hospitalVisit, User doctor);

    Page<HospitalVisit> getPatientVisits(Long patientId, Pageable pageRequest);

    HospitalVisit getById(Long id);

    List<HospitalVisit> getPatientVisitsToday(Long patientId);

    Page<HospitalVisit> findByCriteria(BooleanExpression expression, Pageable pageRequest);

    HospitalVisit switchState(HospitalVisit hospitalVisit, VisitState state);

    void endSession(HospitalVisit hospitalVisit);

    StatDto getVisitStats();
}
