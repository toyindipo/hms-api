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
@Table(name = "pharmacy_session")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = PharmacySession.class)
public class PharmacySession {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotNull
    @JoinColumn(name = "doctor_id")
    @ManyToOne
    private User requestedBy;
    @JoinColumn(name = "pharmacy_id")
    @ManyToOne
    private User processedBy;
    @Column(name="prescription", columnDefinition="TEXT")
    private String prescription;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "Prescribed_Drugs", joinColumns = {
            @JoinColumn(name = "pharmacy_session_id") }, inverseJoinColumns = {
            @JoinColumn(name = "prescription_id") })
    private Set<Prescription> prescriptions;

    @NotNull
    @Column
    private Boolean drugsPurchased = Boolean.FALSE;

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

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public Set<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public Boolean getDrugsPurchased() {
        return drugsPurchased;
    }

    public void setDrugsPurchased(Boolean drugsPurchased) {
        this.drugsPurchased = drugsPurchased;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PharmacySession that = (PharmacySession) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
