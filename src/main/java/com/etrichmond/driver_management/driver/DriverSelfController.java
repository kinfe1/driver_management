package com.etrichmond.driver_management.driver;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etrichmond.driver_management.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/v1/drivers/me", "/api/drivers/me" })
@RequiredArgsConstructor
public class DriverSelfController {

    private final DriverService driverService;
    private final DriverPreferencesService preferencesService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<DriverResponse>> profile(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity
                .ok(ApiResponse.success(driverService.getProfileByEmail(email), "Driver profile fetched successfully"));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<DriverResponse>> updateProfile(Authentication authentication,
            @Valid @RequestBody DriverProfileRequest request) {
        String email = (String) authentication.getPrincipal();
        String driverId = driverService.getEntityByEmail(email).getId();
        return ResponseEntity.ok(ApiResponse.success(driverService.updateProfile(driverId, request),
                "Driver profile updated successfully"));
    }

    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<DriverPreferencesResponse>> preferences(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(preferencesService.getPreferences(email),
                "Driver preferences fetched successfully"));
    }

    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<DriverPreferencesResponse>> updatePreferences(Authentication authentication,
            @Valid @RequestBody DriverPreferencesRequest request) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(preferencesService.updatePreferences(email, request),
                "Driver preferences updated successfully"));
    }
}
