package com.oasis.hms.controller;

import com.oasis.hms.dao.predicate.CustomPredicateBuilder;
import com.oasis.hms.dao.predicate.Operation;
import com.oasis.hms.dto.StatDto;
import com.oasis.hms.dto.TagDto;
import com.oasis.hms.model.HospitalVisit;
import com.oasis.hms.model.Patient;
import com.oasis.hms.model.User;
import com.oasis.hms.model.enums.Role;
import com.oasis.hms.model.enums.VisitState;
import com.oasis.hms.service.*;
import com.oasis.hms.utils.GenericUtil;
import com.oasis.hms.utils.PageUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by Toyin on 2/4/19.
 */
@RestController
@RequestMapping("/api/v1/visits")
public class HospitalController {
    private HospitalVisitService visitService;
    private UserService userService;
    private PatientService patientService;
    private TagService tagService;

    @Autowired
    public HospitalController(HospitalVisitService visitService,
                      UserService userService, PatientService patientService, TagService tagService) {
        this.visitService = visitService;
        this.userService = userService;
        this.patientService = patientService;
        this.tagService = tagService;
    }

    @PostMapping("/patients/{patientId}")
    @PreAuthorize("hasRole('FRONT_DESK')")
    public ResponseEntity<HospitalVisit> createHospitalVisit
            (@AuthenticationPrincipal Principal principal, @PathVariable("patientId") Long patientId) {
        List<HospitalVisit> visits = visitService.getPatientVisitsToday(patientId);
        if (!visits.isEmpty()) {
            boolean sessionsClosed = visits.stream().allMatch(v -> {
                return v.getState().equals(VisitState.END_SESSION);
            });
            if (!sessionsClosed) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        Patient patient = patientService.findById(patientId);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        User user = userService.findOne(principal.getName());
        HospitalVisit visit = visitService.createVisit(patient, user);
        return ResponseEntity.ok(visit);
    }

    @PostMapping("/{visitId}/assign")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<HospitalVisit> assignSession(@AuthenticationPrincipal Principal principal,
             @PathVariable("visitId") Long visitId) {
        User user = GenericUtil.getActiveUser(principal, userService);
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getAssignedTo() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else  {
            tagService.removeFromTagQueue(hospitalVisit.getTagNumber());
            return ResponseEntity.ok(visitService.assignToDoctor(hospitalVisit, user));
        }
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<Page<HospitalVisit>> listPatientVisits(@PathVariable("patientId") Long patientId,
                                                                 @RequestParam(value="page", required = false, defaultValue = "0") int page,
       @RequestParam(value="pageSize", defaultValue = "10") int pageSize) {
        Pageable pageRequest =
                PageUtil.createPageRequest(page, pageSize,
                        Sort.by(Sort.Order.desc("createdAt")));
        return ResponseEntity.ok(visitService.getPatientVisits(patientId, pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalVisit> getHospitalVisit(@PathVariable("id") Long id,
                                                          @AuthenticationPrincipal Principal principal) {
        HospitalVisit visit = visitService.getById(id);
        if (visit == null) {
            return ResponseEntity.notFound().build();
        } else {
            User user = GenericUtil.getActiveUser(principal, userService);
            if (user.getRole().equals(Role.ACCOUNT)) {
                visit.setLabSession(null);

            } else if (user.getRole().equals(Role.PHARMACY)) {
                visit.setLabSession(null);

            } else if (user.getRole().equals(Role.LAB_TECH)) {
                visit.setPharmacySession(null);

            } else if (user.getRole().equals(Role.FRONT_DESK)) {
                visit.setPharmacySession(null);
                visit.setLabSession(null);
            }
            if (visit.getAssignedTo() != null ||
                    visit.getCreatedAt().isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
                //Todo: rather than persist column cache in Redis
                visit.setTagNumber(0);
            }
            if (visit.getPatient().getPicture() != null) {
                visit.getPatient().setPictureBase64(GenericUtil.bytesToBase64(visit.getPatient().getPicture()));
            }
            return ResponseEntity.ok(visit);
        }
    }

    @GetMapping("/patients/{patientId}/today")
    public ResponseEntity<List<HospitalVisit>> getPatientVisitsToday(@PathVariable("patientId") Long patientId) {
        return ResponseEntity.ok(visitService.getPatientVisitsToday(patientId));
    }

    @GetMapping
    public ResponseEntity<Page<HospitalVisit>> listVisits(@RequestParam(value="page", required = false, defaultValue = "0") int page,
         @RequestParam(value="pageSize", defaultValue = "10") int pageSize,
         @RequestParam(name = "firstname", required = false) String firstname,
            @RequestParam(name = "lastname", required = false) String lastname,
         @RequestParam(name = "hospitalNumber", required = false) String hospitalNumber,
          @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
         @RequestParam(name = "state", required = false) VisitState state,
         @RequestParam(value="from", required=false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
         @RequestParam(value="to", required=false) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to) {
        if (from == null) {
            from = LocalDateTime.now();
        }
        if (to == null || to.isBefore(from)) {
            to = from;
        }
        from = GenericUtil.truncateTime(from);
        to = GenericUtil.ceilTime(to);

        BooleanExpression filter = new CustomPredicateBuilder<HospitalVisit>("hospitalVisit", HospitalVisit.class)
                .with("patient.firstname", Operation.LIKE, firstname)
                .with("patient.lastname", Operation.LIKE, lastname)
                .with("patient.hospitalNumber", Operation.LIKE, hospitalNumber)
                .with("patient.phoneNumber", Operation.LIKE, phoneNumber)
                .with("state", Operation.ENUM, state)
                .with("createdAt", Operation.BETWEEN, new LocalDateTime[] {from, to}).build();
        Pageable pageRequest =
                PageUtil.createPageRequest(page, pageSize,
                        Sort.by(Sort.Order.desc("createdAt")));
        return ResponseEntity.ok(visitService.findByCriteria(filter, pageRequest));
    }

    @PostMapping("/{id}/end")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<HospitalVisit> endSession(@AuthenticationPrincipal Principal principal,
                                                         @PathVariable("id") Long visitId) {
        User user = GenericUtil.getActiveUser(principal, userService);
        HospitalVisit hospitalVisit = visitService.getById(visitId);
        if (hospitalVisit == null) {
            return ResponseEntity.notFound().build();
        } else if (hospitalVisit.getState() != VisitState.DOC_SESSION) {
            return ResponseEntity.unprocessableEntity().build();
        } else if (hospitalVisit.getAssignedTo() != null && hospitalVisit.getAssignedTo() != user) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            return ResponseEntity.ok(visitService.switchState(hospitalVisit, VisitState.END_SESSION));
        }
    }

    @GetMapping("/tag/number")
    public ResponseEntity<TagDto> getTagNumber() {
        return ResponseEntity.ok(tagService.getTagNumber());
    }

    @GetMapping("/data/stats")
    public ResponseEntity<StatDto> getStats() {
        return ResponseEntity.ok(visitService.getVisitStats());
    }
}
