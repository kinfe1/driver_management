package com.etrichmond.driver_management.vehicle;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etrichmond.driver_management.common.exception.ResourceNotFoundException;
import com.etrichmond.driver_management.driver.DriverEntity;
import com.etrichmond.driver_management.driver.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleAssignmentService {

    private final VehicleAssignmentRepository vehicleAssignmentRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    @Transactional
    public VehicleAssignmentResponse assign(VehicleAssignmentRequest request) {

        DriverEntity driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        VehicleEntity vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new IllegalArgumentException("Vehicle is not available");
        }

        VehicleAssignmentEntity entity = new VehicleAssignmentEntity();

        entity.setDriver(driver);
        entity.setVehicle(vehicle);
        entity.setAssignmentDate(request.getAssignmentDate());

        vehicle.setStatus(VehicleStatus.ASSIGNED);

        VehicleAssignmentEntity saved = vehicleAssignmentRepository.save(entity);

        vehicleRepository.save(vehicle);

        return toResponse(saved);
    }

    public List<VehicleAssignmentResponse> listAll() {

        return vehicleAssignmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<VehicleAssignmentResponse> getByDriver(String driverId) {

        return vehicleAssignmentRepository.findByDriver_Id(driverId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(String assignmentId) {

        VehicleAssignmentEntity entity = vehicleAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle assignment not found"));

        VehicleEntity vehicle = entity.getVehicle();

        vehicle.setStatus(VehicleStatus.AVAILABLE);

        vehicleRepository.save(vehicle);

        vehicleAssignmentRepository.delete(entity);
    }

    private VehicleAssignmentResponse toResponse(VehicleAssignmentEntity entity) {

        VehicleAssignmentResponse response = new VehicleAssignmentResponse();

        response.setId(entity.getId());

        response.setDriverId(entity.getDriver().getId());

        response.setDriverName(entity.getDriver().getFirstName() + " " + entity.getDriver().getLastName());

        response.setVehicleId(entity.getVehicle().getId());

        response.setPlate(entity.getVehicle().getPlateNumber());

        response.setModel(entity.getVehicle().getModel());

        response.setType(entity.getVehicle().getVehicleType());

        response.setAssignedAt(entity.getAssignmentDate());

        response.setStatus(
                entity.getReleasedDate() == null
                        ? "Assigned"
                        : "Released");

        return response;
    }
}