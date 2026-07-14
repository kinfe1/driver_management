package com.etrichmond.driver_management.driver;

import com.etrichmond.driver_management.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<ApiResponse<DriverResponse>> create(@Valid @RequestBody DriverRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(driverService.create(request), "Driver created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DriverResponse>>> listAll() {
        return ResponseEntity.ok(ApiResponse.success(driverService.listAll(), "Drivers fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(driverService.getById(id), "Driver fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> update(@PathVariable String id,
            @Valid @RequestBody DriverRequest request) {
        return ResponseEntity.ok(ApiResponse.success(driverService.update(id, request), "Driver updated successfully"));
    }
}
