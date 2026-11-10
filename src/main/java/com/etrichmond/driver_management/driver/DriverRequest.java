package com.etrichmond.driver_management.driver;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverRequest {

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;

    private String nationality;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    private String secondaryPhone;

    private String address;

    @NotBlank
    private String employeeId;

    @NotBlank
    private String branch;

    @NotBlank
    private String driverType;

    @NotBlank
    private String licenseNumber;

    private LocalDate licenseExpiry;

    private DriverStatus status = DriverStatus.PENDING_APPROVAL;

    private String photoUrl;
}