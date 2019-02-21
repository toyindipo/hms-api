package com.oasis.hms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.oasis.hms.model.enums.Sex;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Toyin on 1/30/19.
 */

@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name="hospital_no", unique=true)
    private String hospitalNumber;

    @NotNull
    @Size(min=2, max=30, message="{firstName.size}")
    @Column(name="firstname")
    private String firstname;

    @NotNull
    @Size(min=2, max=30, message="{lastName.size}")
    @Column(name="lastname")
    private String lastname;

    @Size(max=30, message="{lastName.size}")
    @Column(name="othername")
    private String othername;

    @Email(message="{email.valid}")
    private String email;

    @Pattern(regexp="(\\+)?[0-9]{11,20}$", message="{phoneNumber.valid}")
    @NotNull
    @Column(name="phone_number")
    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="date_of_birth")
    @Past
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private Sex sex;

    @Column(name="address", columnDefinition="TEXT")
    @NotNull
    private String address;

    @Column
    @Lob
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] picture;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="modified_at")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Transient
    private String pictureBase64;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
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

    public String getPictureBase64() {
        return pictureBase64;
    }

    public void setPictureBase64(String pictureBase64) {
        this.pictureBase64 = pictureBase64;
    }
}
