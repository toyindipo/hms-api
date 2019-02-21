package com.oasis.hms.controller;

import com.oasis.hms.dto.DiagnosisResultDto;
import com.oasis.hms.dto.DocRequestDto;
import com.oasis.hms.model.HospitalVisit;
import com.oasis.hms.model.LabSession;
import com.oasis.hms.model.User;
import com.oasis.hms.model.enums.VisitState;
import com.oasis.hms.service.HospitalVisitService;
import com.oasis.hms.service.LabSessionService;
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

/**
 * Created by Toyin on 2/4/19.
 */
@RestController
@RequestMapping("/api/v1/visits/{visitId}/lab")
public class LabController {
    private HospitalVisitService visitService;
    private LabSessionService labSessionService;
    private UserService userService;

    @Autowired
    public LabController(HospitalVisitService visitService, LabSessionService labSessionService,
                         UserService userService) {
        this.visitService = visitService;
        this.labSessionService = labSessionService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<LabSession> labRequest(@AuthenticationPrincipal Principal principal,
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
            return ResponseEntity.ok(labSessionService.labRequest(hospitalVisit, docRequestDto.getRequest()));
        }
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('LAB_TECH')")
    public ResponseEntity<LabSession> assignLabSession(@AuthenticationPrincipal Principal principal,
               @PathVariable("visitId") Long visitId) {
        User user = GenericUtil.getActiveUser(principal, userService);
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getLabSession().getProcessedBy() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else if (hospitalVisit.getState() != VisitState.LAB_SESSION || hospitalVisit.getLabSession() == null) {
            return ResponseEntity.unprocessableEntity().build();
        }  else  {
            return ResponseEntity.ok(labSessionService.assignSession(hospitalVisit.getLabSession(), user));
        }
    }

    @PostMapping("/result")
    @PreAuthorize("hasRole('LAB_TECH')")
    @Transactional
    public ResponseEntity<LabSession> postLabResult(@AuthenticationPrincipal Principal principal,
                @PathVariable("visitId") Long visitId,
                @RequestBody @Valid DiagnosisResultDto diagnosisResult, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        User user = GenericUtil.getActiveUser(principal, userService);
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getState() != VisitState.LAB_SESSION || hospitalVisit.getLabSession() == null) {
            return ResponseEntity.unprocessableEntity().build();
        } else if (hospitalVisit.getLabSession().getProcessedBy() == null ||
                hospitalVisit.getLabSession().getProcessedBy() != user) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else  {
            LabSession labSession = labSessionService.createDiagnosisResults(hospitalVisit.getLabSession(), diagnosisResult);
            return ResponseEntity.ok(labSession);
        }
    }

    @PostMapping("/end")
    @PreAuthorize("hasRole('LAB_TECH')")
    @Transactional
    public ResponseEntity<HospitalVisit> endLabSession(@AuthenticationPrincipal Principal principal,
                                        @PathVariable("visitId") Long visitId) {
        User user = GenericUtil.getActiveUser(principal, userService);
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getState() != VisitState.LAB_SESSION || hospitalVisit.getLabSession() == null) {
            return ResponseEntity.unprocessableEntity().build();
        } else if (hospitalVisit.getLabSession().getProcessedBy() == null ||
                hospitalVisit.getLabSession().getProcessedBy() != user) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else  {
            visitService.switchState(hospitalVisit, VisitState.DOC_SESSION);
            return ResponseEntity.ok(hospitalVisit);
        }
    }
}
