package com.oasis.hms.service;

import com.oasis.hms.dto.PrescriptionDto;
import com.oasis.hms.model.HospitalVisit;
import com.oasis.hms.model.PharmacySession;
import com.oasis.hms.model.User;

import java.util.Set;

/**
 * Created by Toyin on 2/2/19.
 */
public interface PharmacySessionService {
    PharmacySession prescriptionRequest(HospitalVisit hospitalVisit, String prescription);

    PharmacySession findById(Long id);

    PharmacySession assignSession(PharmacySession pharmacySession, User pharmacist);

    PharmacySession createPrescriptions(PharmacySession pharmacySession, Set<PrescriptionDto> prescriptions);
}
