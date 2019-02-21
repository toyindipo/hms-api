package com.oasis.hms.service.impl;

import com.oasis.hms.dao.PatientDao;
import com.oasis.hms.dto.StatDto;
import com.oasis.hms.model.Patient;
import com.oasis.hms.service.PatientService;
import com.oasis.hms.utils.GenericUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Toyin on 2/3/19.
 */
@Service
public class PatientServiceImpl implements PatientService {
    private PatientDao patientDao;

    @Autowired
    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public Patient registerPatient(Patient patient) {
        return patientDao.saveAndFlush(patient);
    }

    @Override
    public Patient findById(Long id) {
        return patientDao.findById(id).orElse(null);
    }

    @Override
    public Patient findByHospitalNumber(String hospitalNumber) {
        return patientDao.findByHospitalNumber(hospitalNumber);
    }


    @Override
    public Page<Patient> findByCriteria(BooleanExpression expression, Pageable pageRequest) {
        return patientDao.findAll(expression, pageRequest);
    }

    @Override
    public Patient uploadImage(byte[] image, Patient patient) {
        patient.setPicture(image);
        patient.setModifiedAt(LocalDateTime.now());
        return patientDao.saveAndFlush(patient);
    }

    @Override
    public StatDto getRegistrationStats() {
        StatDto stat = new StatDto();
        LocalDateTime now = LocalDateTime.now();
        stat.setToday(patientDao.countByCreatedAtBetween(now.truncatedTo(ChronoUnit.DAYS), now));
        stat.setWeek(patientDao.countByCreatedAtBetween(GenericUtil.getFirstDayOfTheWeek(), now));
        stat.setMonth(patientDao.countByCreatedAtBetween(GenericUtil.getFirstDayOfTheMonth(), now));
        return stat;
    }
}
