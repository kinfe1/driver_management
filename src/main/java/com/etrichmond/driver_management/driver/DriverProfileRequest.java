package com.etrichmond.driver_management.driver;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverProfileRequest {

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    private String phone;

    private String secondaryPhone;

    private String address;

    private String photoUrl;
}