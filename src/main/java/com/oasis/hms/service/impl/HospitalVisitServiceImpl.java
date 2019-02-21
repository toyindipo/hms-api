package com.oasis.hms.service.impl;

import com.oasis.hms.dao.HospitalVisitDao;
import com.oasis.hms.dto.StatDto;
import com.oasis.hms.model.HospitalVisit;
import com.oasis.hms.model.Patient;
import com.oasis.hms.model.User;
import com.oasis.hms.model.enums.VisitState;
import com.oasis.hms.service.HospitalVisitService;
import com.oasis.hms.service.TagService;
import com.oasis.hms.utils.GenericUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by Toyin on 2/3/19.
 */
@Service
public class HospitalVisitServiceImpl implements HospitalVisitService {
    private HospitalVisitDao visitDao;
    private TagService tagService;

    @Autowired
    public HospitalVisitServiceImpl(HospitalVisitDao visitDao, TagService tagService) {
        this.visitDao = visitDao;
        this.tagService = tagService;
    }

    @Override
    public HospitalVisit createVisit(Patient patient, User deskOfficer) {
        HospitalVisit visit = new HospitalVisit();
        visit.setState(VisitState.INITIAL_STATE);
        visit.setPatient(patient);
        visit.setCreatedBy(deskOfficer);
        visit.setTagNumber(tagService.incTagNumber());
        return visitDao.saveAndFlush(visit);
    }

    @Override
    public HospitalVisit assignToDoctor(HospitalVisit hospitalVisit, User doctor) {
        hospitalVisit.setAssignedTo(doctor);
        hospitalVisit.setState(VisitState.DOC_SESSION);
        hospitalVisit.setModifiedAt(LocalDateTime.now());
        return visitDao.saveAndFlush(hospitalVisit);
    }

    @Override
    public Page<HospitalVisit> getPatientVisits(Long patientId, Pageable pageRequest) {
        return visitDao.findByPatientId(patientId, pageRequest);
    }

    @Override
    public HospitalVisit getById(Long id) {
        return visitDao.findById(id).orElse(null);
    }

    @Override
    public List<HospitalVisit> getPatientVisitsToday(Long patientId) {
        return visitDao.findByPatientIdAndCreatedAtBetween(patientId,
                GenericUtil.truncateTime(LocalDateTime.now()), GenericUtil.ceilTime(LocalDateTime.now()));
    }

    @Override
    public Page<HospitalVisit> findByCriteria(BooleanExpression expression, Pageable pageRequest) {
        return visitDao.findAll(expression, pageRequest);
    }

    @Override
    public HospitalVisit switchState(HospitalVisit hospitalVisit, VisitState state) {
        hospitalVisit.setState(state);
        hospitalVisit.setModifiedAt(LocalDateTime.now());
        return visitDao.saveAndFlush(hospitalVisit);
    }

    @Override
    public void endSession(HospitalVisit hospitalVisit) {
        hospitalVisit.setState(VisitState.END_SESSION);
        hospitalVisit.setModifiedAt(LocalDateTime.now());
        visitDao.saveAndFlush(hospitalVisit);
    }

    @Override
    public StatDto getVisitStats() {
        StatDto stat = new StatDto();
        LocalDateTime now = LocalDateTime.now();
        stat.setToday(visitDao.countByCreatedAtBetween(now.truncatedTo(ChronoUnit.DAYS), now));
        stat.setWeek(visitDao.countByCreatedAtBetween(GenericUtil.getFirstDayOfTheWeek(), now));
        stat.setMonth(visitDao.countByCreatedAtBetween(GenericUtil.getFirstDayOfTheMonth(), now));
        return stat;
    }
}
