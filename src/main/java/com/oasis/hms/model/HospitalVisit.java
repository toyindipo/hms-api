package com.oasis.hms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.oasis.hms.model.enums.VisitState;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by Toyin on 1/30/19.
 */
@Entity
@Table(name = "hospital_visit")
public class HospitalVisit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @JoinColumn(name = "doctor_id")
    @ManyToOne
    private User assignedTo;

    @NotNull
    @JoinColumn(name = "desk_officer_id")
    @ManyToOne
    private User createdBy;

    @Column(name="report", columnDefinition="TEXT")
    private String report;

    @OneToOne
    @JoinColumn(name = "lab_session_id", referencedColumnName = "id")
    private LabSession labSession;

    @OneToOne
    @JoinColumn(name = "pharmacy_session_id", referencedColumnName = "id")
    private PharmacySession pharmacySession;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private VisitState state;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="modified_at")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Column
    private int tagNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public LabSession getLabSession() {
        return labSession;
    }

    public void setLabSession(LabSession labSession) {
        this.labSession = labSession;
    }

    public PharmacySession getPharmacySession() {
        return pharmacySession;
    }

    public void setPharmacySession(PharmacySession pharmacySession) {
        this.pharmacySession = pharmacySession;
    }

    public VisitState getState() {
        return state;
    }

    public void setState(VisitState state) {
        this.state = state;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public int getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(int tagNumber) {
        this.tagNumber = tagNumber;
    }
}
