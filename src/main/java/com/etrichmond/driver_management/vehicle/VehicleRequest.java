package com.etrichmond.driver_management.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleRequest {

    @NotBlank
    private String plateNumber;

    @NotBlank
    private String model;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String vehicleType;

    @NotNull
    private Integer year;

    private VehicleStatus status = VehicleStatus.AVAILABLE;
}