package com.oasis.hms.controller;

import com.oasis.hms.dao.predicate.CustomPredicateBuilder;
import com.oasis.hms.dao.predicate.Operation;
import com.oasis.hms.dto.StatDto;
import com.oasis.hms.model.Patient;
import com.oasis.hms.service.PatientService;
import com.oasis.hms.utils.GenericUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by Toyin on 2/5/19.
 */
@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {
    private PatientService patientService;
    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PreAuthorize("hasRole('FRONT_DESK')")
    @PostMapping
    public ResponseEntity<Patient> registerPatient(@RequestBody @Valid Patient patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else if (patientService.findByHospitalNumber(patient.getHospitalNumber()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(patientService.registerPatient(patient));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Patient> findById(@PathVariable(value = "id") Long id){
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        } else if (patient.getPicture() != null) {
            patient.setPictureBase64(GenericUtil.bytesToBase64(patient.getPicture()));
        }
        return ResponseEntity.ok(patient);
    }

    @GetMapping
    public ResponseEntity<Page<Patient>> listPatients(@RequestParam(value="page", required = false, defaultValue = "0") int page,
               @RequestParam(value="pageSize", defaultValue = "10") int pageSize, @RequestParam(value="firstname", required = false) String firstname,
               @RequestParam(value="lastname", required = false) String lastname, @RequestParam(value="hospitalNumber", required = false) String hospitalNumber,
               @RequestParam(value="phoneNumber", required = false) String phoneNumber) {
        BooleanExpression filter = new CustomPredicateBuilder<Patient>("patient", Patient.class)
                .with("firstname", Operation.LIKE, firstname)
                .with("lastname", Operation.LIKE, lastname)
                .with("hospitalNumber", Operation.LIKE, hospitalNumber)
                .with("phoneNumber", Operation.LIKE, phoneNumber).build();
        Pageable pageRequest =
                PageUtil.createPageRequest(page, pageSize,
                        Sort.by(Sort.Order.asc("firstname"), Sort.Order.asc("lastname")));
        return ResponseEntity.ok(patientService.findByCriteria(filter, pageRequest));
    }

    @PreAuthorize("hasRole('FRONT_DESK')")
    @PostMapping("/{id}/image")
    public ResponseEntity<Patient> uploadImage(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        } else {
            try {
                return ResponseEntity.ok(patientService.uploadImage(file.getBytes(), patient));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    }

    @GetMapping("/data/stats")
    public ResponseEntity<StatDto> getStats() {
        return ResponseEntity.ok(patientService.getRegistrationStats());
    }
}
