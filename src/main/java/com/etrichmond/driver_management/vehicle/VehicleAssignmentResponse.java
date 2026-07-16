package com.etrichmond.driver_management.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleAssignmentResponse {
    private String id;
    private String driverId;
    private String vehicleId;
    private String assignmentDate;
}
