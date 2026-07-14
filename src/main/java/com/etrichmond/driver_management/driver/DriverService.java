package com.etrichmond.driver_management.driver;

import com.etrichmond.driver_management.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverResponse create(DriverRequest request) {
        DriverEntity entity = new DriverEntity();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setLicenseNumber(request.getLicenseNumber());
        entity.setStatus(request.getStatus());

        DriverEntity saved = driverRepository.save(entity);
        return toResponse(saved);
    }

    public List<DriverResponse> listAll() {
        return driverRepository.findAll().stream().map(this::toResponse).toList();
    }

    public DriverResponse getById(String id) {
        return driverRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }

    public DriverResponse update(String id, DriverRequest request) {
        DriverEntity entity = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setLicenseNumber(request.getLicenseNumber());
        entity.setStatus(request.getStatus());

        return toResponse(driverRepository.save(entity));
    }

    private DriverResponse toResponse(DriverEntity entity) {
        return new DriverResponse(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getPhone(), entity.getLicenseNumber(), entity.getStatus());
    }
}
