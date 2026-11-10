package com.etrichmond.driver_management.admin;

import lombok.Data;

@Data
public class DashboardStatsResponse {
    private int totalDrivers;
    private int activeDrivers;
    private int suspendedDrivers;
    private int expiredLicenses;
}
