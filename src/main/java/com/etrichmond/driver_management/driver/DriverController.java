package com.etrichmond.driver_management.driver;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.etrichmond.driver_management.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/drivers", "/api/v1/drivers", "/api/v1/admin/drivers" })
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<ApiResponse<DriverResponse>> create(
            @Valid @RequestBody DriverRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        driverService.create(request),
                        "Driver created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DriverResponse>>> listAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) DriverStatus status,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        driverService.search(search, status, page, limit),
                        "Drivers fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> getById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        driverService.getById(id),
                        "Driver fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody DriverRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        driverService.update(id, request),
                        "Driver updated successfully"));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<DriverResponse>> approve(
            @PathVariable String id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        driverService.approve(id),
                        "Driver approved successfully"));
    }

    @PostMapping(value = "/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadPhoto(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        driverService.uploadPhoto(file),
                        "Driver photo uploaded successfully"));
    }
}