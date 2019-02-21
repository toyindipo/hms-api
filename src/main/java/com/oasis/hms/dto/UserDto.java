package com.oasis.hms.dto;

import com.oasis.hms.model.enums.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {
    @Size(min=6, message="{password.size}")
    private String password;

    @NotNull
    @Size(min=2, max=30, message="{firstName.size}")
    private String firstname;

    @NotNull
    @Size(min=2, max=30, message="{lastName.size}")
    private String lastname;

    @NotNull
    @Email(message="{email.valid}")
    private String email;

    @NotNull
    @Pattern(regexp="(\\+)?[0-9]{11,20}$", message="{phoneNumber.valid}")
    private String phoneNumber;

    @NotNull
    private Role role;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
