package com.etrichmond.driver_management.vehicle;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etrichmond.driver_management.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/vehicle-assignments", "/api/v1/vehicle-assignments" })
@RequiredArgsConstructor
public class VehicleAssignmentController {

    private final VehicleAssignmentService vehicleAssignmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleAssignmentResponse>>> listAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        vehicleAssignmentService.listAll(),
                        "Vehicle assignments fetched successfully"));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<ApiResponse<List<VehicleAssignmentResponse>>> getByDriver(
            @PathVariable String driverId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        vehicleAssignmentService.getByDriver(driverId),
                        "Driver assignments fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleAssignmentResponse>> assign(
            @Valid @RequestBody VehicleAssignmentRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                vehicleAssignmentService.assign(request),
                                "Vehicle assigned successfully"));
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable String assignmentId) {

        vehicleAssignmentService.delete(assignmentId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        assignmentId,
                        "Vehicle assignment removed successfully"));
    }
}