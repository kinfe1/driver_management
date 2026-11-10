package com.etrichmond.driver_management.vehicle;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etrichmond.driver_management.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleResponse create(VehicleRequest request) {

        if (vehicleRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new IllegalArgumentException("Vehicle with this plate number already exists");
        }

        VehicleEntity entity = new VehicleEntity();

        entity.setPlateNumber(request.getPlateNumber());
        entity.setModel(request.getModel());
        entity.setManufacturer(request.getManufacturer());
        entity.setVehicleType(request.getVehicleType());
        entity.setYear(request.getYear());

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        VehicleEntity saved = vehicleRepository.save(entity);

        return toResponse(saved);
    }

    public List<VehicleResponse> listAll() {

        return vehicleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<VehicleResponse> listAvailable() {

        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VehicleResponse getById(String id) {

        VehicleEntity vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        return toResponse(vehicle);
    }

    public VehicleResponse update(String id, VehicleRequest request) {

        VehicleEntity vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        vehicle.setPlateNumber(request.getPlateNumber());
        vehicle.setModel(request.getModel());
        vehicle.setManufacturer(request.getManufacturer());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setYear(request.getYear());

        if (request.getStatus() != null) {
            vehicle.setStatus(request.getStatus());
        }

        return toResponse(vehicleRepository.save(vehicle));
    }

    public void delete(String id) {

        VehicleEntity vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        vehicleRepository.delete(vehicle);
    }

    private VehicleResponse toResponse(VehicleEntity entity) {

        return new VehicleResponse(
                entity.getId(),
                entity.getPlateNumber(),
                entity.getModel(),
                entity.getManufacturer(),
                entity.getVehicleType(),
                entity.getYear(),
                entity.getStatus());
    }
}