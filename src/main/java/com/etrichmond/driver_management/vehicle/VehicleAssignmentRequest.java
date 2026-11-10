package com.etrichmond.driver_management.vehicle;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleAssignmentRequest {

    @NotNull
    private String driverId;

    @NotNull
    private String vehicleId;

    @NotNull
    private LocalDate assignmentDate;
}