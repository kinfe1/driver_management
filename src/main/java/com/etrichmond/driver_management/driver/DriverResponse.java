package com.etrichmond.driver_management.driver;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverResponse {

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

    private String gender;

    private String nationality;

    private LocalDate licenseExpiry;

    private DriverStatus status;

    private String photoUrl;

    private String assignedVehicle;

    private int profileCompleteness;

}