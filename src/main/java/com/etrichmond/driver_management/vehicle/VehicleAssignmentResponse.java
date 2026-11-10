package com.etrichmond.driver_management.vehicle;

import java.time.LocalDate;

import lombok.Data;

@Data
public class VehicleAssignmentResponse {

    private String id;

    private String driverId;

    private String driverName;

    private String vehicleId;

    private String plate;

    private String type;

    private String model;

    private LocalDate assignedAt;

    private String status;

}