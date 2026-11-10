package com.etrichmond.driver_management.driver;

import java.util.List;

import lombok.Data;

@Data
public class DriverVehicleResponse {
    private String plate;
    private String model;
    private String route;
    private String status;
    private List<VehicleLogResponse> recentVehicleLog;
}
