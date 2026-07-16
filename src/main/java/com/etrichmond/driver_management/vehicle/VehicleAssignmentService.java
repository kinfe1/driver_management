package com.etrichmond.driver_management.vehicle;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleAssignmentService {

    private final VehicleAssignmentRepository vehicleAssignmentRepository;

    public VehicleAssignmentResponse assign(VehicleAssignmentRequest request) {
        VehicleAssignmentEntity entity = new VehicleAssignmentEntity();
        entity.setDriverId(request.getDriverId());
        entity.setVehicleId(request.getVehicleId());
        entity.setAssignmentDate(request.getAssignmentDate());

        VehicleAssignmentEntity saved = vehicleAssignmentRepository.save(entity);
        return new VehicleAssignmentResponse(saved.getId(), saved.getDriverId(), saved.getVehicleId(),
                saved.getAssignmentDate());
    }
}
