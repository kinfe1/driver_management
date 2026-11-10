package com.etrichmond.driver_management.driver;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverPreferencesRequest {

    @NotBlank
    private String language;

    @NotBlank
    private String timezone;

    @NotBlank
    private String notificationMethod;
}
