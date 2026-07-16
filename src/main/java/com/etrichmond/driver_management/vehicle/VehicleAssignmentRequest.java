package com.etrichmond.driver_management.vehicle;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehicleAssignmentRequest {

    @NotBlank
    private String driverId;

    @NotBlank
    private String vehicleId;

    @NotBlank
    private String assignmentDate;
}
