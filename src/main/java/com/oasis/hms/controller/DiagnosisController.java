package com.oasis.hms.controller;

import com.oasis.hms.dao.predicate.CustomPredicateBuilder;
import com.oasis.hms.dao.predicate.Operation;
import com.oasis.hms.model.Diagnosis;
import com.oasis.hms.service.DiagnosisService;
import com.oasis.hms.utils.PageUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Toyin on 2/7/19.
 */
@RestController
@RequestMapping("/api/v1/diagnosis")
public class DiagnosisController {
    private DiagnosisService diagnosisService;

    @Autowired
    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_TECH')")
    @PostMapping
    public ResponseEntity<Diagnosis> createDiagnosis(@RequestBody @Valid Diagnosis diagnosis,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            if (diagnosisService.findByName(diagnosis.getName()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                return ResponseEntity.ok(diagnosisService.createDiagnosis(diagnosis));
            }
        }
    }

    @GetMapping
    public ResponseEntity<Page<Diagnosis>> listDiagnosis(@RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                     @RequestParam(value="pageSize", defaultValue = "10") int pageSize, @RequestParam(value="name", required = false) String name) {
        BooleanExpression filter = new CustomPredicateBuilder<Diagnosis>("diagnosis", Diagnosis.class)
                .with("name", Operation.LIKE, name).build();
        Pageable pageRequest =
                PageUtil.createPageRequest(page, pageSize,
                        Sort.by(Sort.Order.asc("name")));
        return ResponseEntity.ok(diagnosisService.findByCriteria(filter, pageRequest));
    }
}
