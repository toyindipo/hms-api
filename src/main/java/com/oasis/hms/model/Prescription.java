package com.oasis.hms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Toyin on 1/31/19.
 */
@Entity
@Table(name = "prescription")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Prescription.class)
public class Prescription {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name="drug_id")
    private Drug drug;
    @NotNull
    @Column
    private int count;
    @Transient
    private int sold;
    @Column
    @Size(max = 255)
    private String dosage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
}
