package com.etrichmond.driver_management.driver;

import lombok.Data;

@Data
public class DriverPreferencesResponse {
    private String language;
    private String timezone;
    private String notificationMethod;
}
