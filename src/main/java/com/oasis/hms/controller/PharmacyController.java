package com.oasis.hms.controller;

import com.oasis.hms.dto.DocRequestDto;
import com.oasis.hms.dto.PrescriptionDto;
import com.oasis.hms.model.HospitalVisit;
import com.oasis.hms.model.PharmacySession;
import com.oasis.hms.model.Prescription;
import com.oasis.hms.model.User;
import com.oasis.hms.model.enums.VisitState;
import com.oasis.hms.service.DrugService;
import com.oasis.hms.service.HospitalVisitService;
import com.oasis.hms.service.PharmacySessionService;
import com.oasis.hms.service.UserService;
import com.oasis.hms.utils.GenericUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

/**
 * Created by Toyin on 2/4/19.
 */
@RestController
@RequestMapping("/api/v1/visits/{visitId}/pharmacy")
public class PharmacyController {
    private HospitalVisitService visitService;
    private PharmacySessionService pharmacySessionService;
    private UserService userService;
    private DrugService drugService;

    @Autowired
    public PharmacyController(HospitalVisitService visitService,
                              PharmacySessionService pharmacySessionService,
                              UserService userService, DrugService drugService) {
        this.visitService = visitService;
        this.pharmacySessionService = pharmacySessionService;
        this.userService = userService;
        this.drugService = drugService;
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PharmacySession> prescriptionRequest(@AuthenticationPrincipal Principal principal,
                                                         @PathVariable("visitId") Long visitId,
                                                         @RequestBody DocRequestDto docRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        User user = GenericUtil.getActiveUser(principal, userService);
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getState() != VisitState.DOC_SESSION) {
            return ResponseEntity.unprocessableEntity().build();
        } else if (hospitalVisit.getAssignedTo() == null || hospitalVisit.getAssignedTo() != user) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            return ResponseEntity.ok(pharmacySessionService.prescriptionRequest(hospitalVisit, docRequestDto.getRequest()));
        }
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('PHARMACY')")
    public ResponseEntity<PharmacySession> assignPharmacySession(@AuthenticationPrincipal Principal principal,
                                                       @PathVariable("visitId") Long visitId) {
        User user = GenericUtil.getActiveUser(principal, userService);
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getPharmacySession().getProcessedBy() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else if (hospitalVisit.getState() != VisitState.PHARM_SESSION || hospitalVisit.getPharmacySession() == null) {
            return ResponseEntity.unprocessableEntity().build();
        }  else  {
            return ResponseEntity.ok(pharmacySessionService.assignSession(hospitalVisit.getPharmacySession(), user));
        }
    }

    @PostMapping("/prescription")
    @PreAuthorize("hasRole('PHARMACY')")
    @Transactional
    public ResponseEntity<PharmacySession> createPrescriptions(@AuthenticationPrincipal Principal principal,
             @PathVariable("visitId") Long visitId,
             @RequestBody @Valid Set<PrescriptionDto> prescriptionSet, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        User user = GenericUtil.getActiveUser(principal, userService);
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getState() != VisitState.PHARM_SESSION || hospitalVisit.getPharmacySession() == null) {
            return ResponseEntity.unprocessableEntity().build();
        } else if (hospitalVisit.getPharmacySession().getProcessedBy() == null ||
                hospitalVisit.getPharmacySession().getProcessedBy() != user) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else  {
            PharmacySession pharmacySession =
                    pharmacySessionService.createPrescriptions(hospitalVisit.getPharmacySession(), prescriptionSet);
            return ResponseEntity.ok(pharmacySession);
        }
    }

    @PostMapping("/end")
    @PreAuthorize("hasRole('PHARMACY')")
    @Transactional
    public ResponseEntity<HospitalVisit> endPrescription(@AuthenticationPrincipal Principal principal,
                                                       @PathVariable("visitId") Long visitId) {
        User user = GenericUtil.getActiveUser(principal, userService);
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getState() != VisitState.PHARM_SESSION || hospitalVisit.getPharmacySession() == null) {
            return ResponseEntity.unprocessableEntity().build();
        } else if (hospitalVisit.getPharmacySession().getProcessedBy() == null ||
                hospitalVisit.getPharmacySession().getProcessedBy() != user) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else  {
            visitService.switchState(hospitalVisit, VisitState.ACCOUNT_SESSION);
            return ResponseEntity.ok(hospitalVisit);
        }
    }

    @PostMapping("/prescription/sold")
    @PreAuthorize("hasRole('ACCOUNT')")
    @Transactional
    public ResponseEntity<Set<Prescription>> confirmDrugSale(@PathVariable("visitId") Long visitId) {
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getState() != VisitState.ACCOUNT_SESSION || hospitalVisit.getPharmacySession() == null) {
            return ResponseEntity.unprocessableEntity().build();
        } else  {
            Set<Prescription> prescriptions = hospitalVisit.getPharmacySession().getPrescriptions();
            if (prescriptions != null && !prescriptions.isEmpty()) {
                prescriptions = drugService.depleteInventory(prescriptions);
            }
            visitService.switchState(hospitalVisit, VisitState.DOC_SESSION);
            return ResponseEntity.ok(prescriptions);
        }
    }
}
