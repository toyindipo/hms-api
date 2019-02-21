package com.oasis.hms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by Toyin on 1/30/19.
 */

@Entity
@Table(name = "lab_session")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = LabSession.class)
public class LabSession {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    @JoinColumn(name = "doctor_id")
    @ManyToOne
    private User requestedBy;
    @JoinColumn(name = "lab_tech_id")
    @ManyToOne
    private User processedBy;
    @Column(name="lab_request", columnDefinition="TEXT")
    private String labRequest;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "Diagnosis_Results", joinColumns = {
            @JoinColumn(name = "lab_session_id") }, inverseJoinColumns = {
            @JoinColumn(name = "diagnosis_result_id") })
    private Set<DiagnosisResult> diagnosisResults;
    @Column(name="lab_report", columnDefinition="TEXT")
    private String labReport;
    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="modified_at")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(User requestedBy) {
        this.requestedBy = requestedBy;
    }

    public User getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
    }

    public String getLabRequest() {
        return labRequest;
    }

    public void setLabRequest(String labRequest) {
        this.labRequest = labRequest;
    }

    public Set<DiagnosisResult> getDiagnosisResults() {
        return diagnosisResults;
    }

    public void setDiagnosisResults(Set<DiagnosisResult> diagnosisResults) {
        this.diagnosisResults = diagnosisResults;
    }

    public String getLabReport() {
        return labReport;
    }

    public void setLabReport(String labReport) {
        this.labReport = labReport;
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
}
