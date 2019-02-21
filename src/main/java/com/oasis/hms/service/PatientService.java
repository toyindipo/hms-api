package com.oasis.hms.service;

import com.oasis.hms.dto.StatDto;
import com.oasis.hms.model.Patient;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Toyin on 2/2/19.
 */
public interface PatientService {
    Patient registerPatient(Patient patient);

    Patient findById(Long id);

    Patient findByHospitalNumber(String hospitalNumber);

    Page<Patient> findByCriteria(BooleanExpression expression, Pageable pageRequest);

    Patient uploadImage(byte[] image, Patient patient);

    StatDto getRegistrationStats();
}
