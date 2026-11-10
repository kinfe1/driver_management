package com.etrichmond.driver_management.vehicle;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etrichmond.driver_management.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/vehicles", "/api/v1/vehicles" })
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> create(
            @Valid @RequestBody VehicleRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        vehicleService.create(request),
                        "Vehicle created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> listAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        vehicleService.listAll(),
                        "Vehicles fetched successfully"));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> listAvailable() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        vehicleService.listAvailable(),
                        "Available vehicles fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        vehicleService.getById(id),
                        "Vehicle fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody VehicleRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        vehicleService.update(id, request),
                        "Vehicle updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable String id) {

        vehicleService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        id,
                        "Vehicle deleted successfully"));
    }
}