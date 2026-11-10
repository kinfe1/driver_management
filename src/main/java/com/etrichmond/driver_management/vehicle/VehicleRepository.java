package com.etrichmond.driver_management.vehicle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {

    boolean existsByPlateNumber(String plateNumber);

    List<VehicleEntity> findByStatus(VehicleStatus status);

}