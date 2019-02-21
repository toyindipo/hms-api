package com.oasis.hms.service.impl;

import com.oasis.hms.dao.DiagnosisDao;
import com.oasis.hms.dao.DiagnosisResultDao;
import com.oasis.hms.dao.HospitalVisitDao;
import com.oasis.hms.dao.LabSessionDao;
import com.oasis.hms.dto.DiagnosisResultDto;
import com.oasis.hms.model.*;
import com.oasis.hms.model.enums.VisitState;
import com.oasis.hms.service.LabSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Toyin on 2/3/19.
 */
@Service
public class LabSessionServiceImpl implements LabSessionService {
    private LabSessionDao labSessionDao;
    private HospitalVisitDao visitDao;
    private DiagnosisResultDao diagnosisResultDao;
    private DiagnosisDao diagnosisDao;

    @Autowired
    public LabSessionServiceImpl(LabSessionDao labSessionDao, HospitalVisitDao visitDao,
                                 DiagnosisResultDao diagnosisResultDao, DiagnosisDao diagnosisDao) {
        this.labSessionDao = labSessionDao;
        this.visitDao = visitDao;
        this.diagnosisResultDao = diagnosisResultDao;
        this.diagnosisDao = diagnosisDao;
    }

    @Override
    @Transactional
    public LabSession labRequest(HospitalVisit hospitalVisit, String labRequest) {
        LabSession labSession =  hospitalVisit.getLabSession();
        if (labSession == null) {
            labSession = new LabSession();
            labSession.setRequestedBy(hospitalVisit.getAssignedTo());
            labSession.setLabRequest(labRequest);
            labSession = labSessionDao.saveAndFlush(labSession);
            hospitalVisit.setLabSession(labSession);
            hospitalVisit.setState(VisitState.LAB_SESSION);
            hospitalVisit.setModifiedAt(LocalDateTime.now());
            visitDao.saveAndFlush(hospitalVisit);
        } else {
            labSession.setLabRequest(labRequest);
            labSession.setModifiedAt(LocalDateTime.now());
            labSession = labSessionDao.saveAndFlush(labSession);
        }
        return labSession;
    }

    @Override
    public LabSession findById(Long id) {
        return labSessionDao.findById(id).orElse(null);
    }

    @Override
    public LabSession assignSession(LabSession labSession, User labTech) {
        labSession.setProcessedBy(labTech);
        labSession.setModifiedAt(LocalDateTime.now());
        return labSessionDao.saveAndFlush(labSession);
    }

    @Override
    @Transactional
    public LabSession createDiagnosisResults(LabSession labSession, DiagnosisResultDto diagnosisResult) {
        Set<DiagnosisResult> prevResults = labSession.getDiagnosisResults();
        Set<DiagnosisResult> newResults = new HashSet<>();
        diagnosisResult.getDiagnosisSet().forEach(d -> {
            diagnosisDao.findById(d.getDiagnosisId()).ifPresent(diagnosis -> {
                Optional<DiagnosisResult> duplicate = prevResults.stream()
                        .filter(r -> r.getDiagnosis().equals(diagnosis)).findFirst();
                DiagnosisResult result;
                if (duplicate.isPresent()) {
                    result = duplicate.get();
                    result.setStatus(d.getStatus());
                } else {
                    result = new DiagnosisResult();
                    result.setDiagnosis(diagnosis);
                    result.setStatus(d.getStatus());
                }
                if (result.getId() < 1) {
                    newResults.add(diagnosisResultDao.saveAndFlush(result));
                } else {
                    diagnosisResultDao.saveAndFlush(result);
                }
            });
        });
        labSession.setLabReport(diagnosisResult.getLabReport());
        if (!newResults.isEmpty()) {
            prevResults.addAll(newResults);
            labSession.setModifiedAt(LocalDateTime.now());
            labSession = labSessionDao.saveAndFlush(labSession);
        }
        return labSession;
    }
}
