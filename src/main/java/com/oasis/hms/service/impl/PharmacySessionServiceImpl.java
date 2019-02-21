package com.oasis.hms.service.impl;

import com.oasis.hms.dao.DrugDao;
import com.oasis.hms.dao.HospitalVisitDao;
import com.oasis.hms.dao.PharmacySessionDao;
import com.oasis.hms.dao.PrescriptionDao;
import com.oasis.hms.dto.PrescriptionDto;
import com.oasis.hms.model.HospitalVisit;
import com.oasis.hms.model.PharmacySession;
import com.oasis.hms.model.Prescription;
import com.oasis.hms.model.User;
import com.oasis.hms.model.enums.VisitState;
import com.oasis.hms.service.PharmacySessionService;
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
public class PharmacySessionServiceImpl implements PharmacySessionService {
    private PharmacySessionDao pharmacySessionDao;
    private HospitalVisitDao visitDao;
    private PrescriptionDao prescriptionDao;
    private DrugDao drugDao;

    @Autowired
    public PharmacySessionServiceImpl(PharmacySessionDao pharmacySessionDao,
                                      HospitalVisitDao visitDao, PrescriptionDao prescriptionDao, DrugDao drugDao) {
        this.pharmacySessionDao = pharmacySessionDao;
        this.visitDao = visitDao;
        this.prescriptionDao = prescriptionDao;
        this.drugDao = drugDao;
    }

    @Override
    public PharmacySession prescriptionRequest(HospitalVisit hospitalVisit, String prescription) {
        PharmacySession session = hospitalVisit.getPharmacySession();
        if (session == null) {
            session = new PharmacySession();
            session.setPrescription(prescription);
            session.setRequestedBy(hospitalVisit.getAssignedTo());
            session = pharmacySessionDao.saveAndFlush(session);
            hospitalVisit.setState(VisitState.PHARM_SESSION);
            hospitalVisit.setPharmacySession(session);
            hospitalVisit.setModifiedAt(LocalDateTime.now());
            visitDao.saveAndFlush(hospitalVisit);
        } else {
            session.setPrescription(prescription);
            session.setModifiedAt(LocalDateTime.now());
            session = pharmacySessionDao.saveAndFlush(session);
        }

        return session;
    }

    @Override
    public PharmacySession findById(Long id) {
        return pharmacySessionDao.findById(id).orElse(null);
    }

    @Override
    public PharmacySession assignSession(PharmacySession pharmacySession, User pharmacist) {
        pharmacySession.setProcessedBy(pharmacist);
        pharmacySession.setModifiedAt(LocalDateTime.now());
        return pharmacySessionDao.saveAndFlush(pharmacySession);
    }

    @Override
    @Transactional
    public PharmacySession createPrescriptions(PharmacySession pharmacySession, Set<PrescriptionDto> prescriptionSet) {
        Set<Prescription> prevPrescriptions = pharmacySession.getPrescriptions();
        Set<Prescription> newPrescriptions = new HashSet<>();
        prescriptionSet.forEach(p -> {
            drugDao.findById(p.getDrugId()).ifPresent(drug -> {
                Optional<Prescription> duplicate = prevPrescriptions.stream()
                        .filter(pp -> pp.getDrug().equals(drug)).findFirst();
                Prescription prescription;
                if (duplicate.isPresent()) {
                    prescription = duplicate.get();
                    prescription.setCount(prescription.getCount() + p.getCount());
                    prescription.setDosage(prescription.getDosage());
                } else {
                    prescription = new Prescription();
                    prescription.setDrug(drug);
                    prescription.setCount(p.getCount());
                    prescription.setDosage(p.getDosage());
                }
                if (prescription.getCount() > 1) {
                    if (prescription.getId() < 1) {
                        newPrescriptions.add(prescriptionDao.saveAndFlush(prescription));
                    } else {
                        prescriptionDao.saveAndFlush(prescription);
                    }
                }
            });
        });
        if (!newPrescriptions.isEmpty()) {
            prevPrescriptions.addAll(newPrescriptions);
            pharmacySession.setModifiedAt(LocalDateTime.now());
            pharmacySession = pharmacySessionDao.saveAndFlush(pharmacySession);
        }
        return pharmacySession;
    }
}
