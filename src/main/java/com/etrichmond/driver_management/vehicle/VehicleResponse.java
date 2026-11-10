package com.etrichmond.driver_management.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleResponse {

    private String id;

    private String plateNumber;

    private String model;

    private String manufacturer;

    private String vehicleType;

    private Integer year;

    private VehicleStatus status;
}