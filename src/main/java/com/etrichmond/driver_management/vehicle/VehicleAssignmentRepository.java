package com.etrichmond.driver_management.vehicle;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleAssignmentRepository
        extends JpaRepository<VehicleAssignmentEntity, String> {

    List<VehicleAssignmentEntity> findByDriver_Id(String driverId);

    Optional<VehicleAssignmentEntity> findByDriverIdAndReleasedDateIsNull(String driverId);

}