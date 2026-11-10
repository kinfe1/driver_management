package com.etrichmond.driver_management.driver;

import lombok.Data;

@Data
public class VehicleLogResponse {
    private String inspectionDate;
    private String fuelRefillNotes;
    private String checkSummary;
}
