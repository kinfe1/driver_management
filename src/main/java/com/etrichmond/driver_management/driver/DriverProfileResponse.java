package com.etrichmond.driver_management.driver;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DriverProfileResponse {

    private String id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phone;

    private String secondaryPhone;

    private String address;

    private String employeeId;

    private String branch;

    private String driverType;

    private String licenseNumber;

    private LocalDate licenseExpiry;

    private DriverStatus status;

    private String assignedVehicle;

    private String photoUrl;

    private int profileCompleteness;
}