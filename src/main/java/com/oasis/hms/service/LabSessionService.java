package com.oasis.hms.service;

import com.oasis.hms.dto.DiagnosisDto;
import com.oasis.hms.dto.DiagnosisResultDto;
import com.oasis.hms.model.HospitalVisit;
import com.oasis.hms.model.LabSession;
import com.oasis.hms.model.User;

import java.util.Set;

/**
 * Created by Toyin on 2/2/19.
 */
public interface LabSessionService {
    LabSession labRequest(HospitalVisit hospitalVisit, String labRequest);

    LabSession findById(Long id);

    LabSession assignSession(LabSession labSession, User labTech);

    LabSession createDiagnosisResults(LabSession labSession, DiagnosisResultDto diagnosisResults);
}
