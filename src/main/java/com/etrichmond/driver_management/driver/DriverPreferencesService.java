package com.etrichmond.driver_management.driver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverPreferencesService {

    private final DriverService driverService;
    private final Map<String, DriverPreferencesResponse> preferences = new ConcurrentHashMap<>();

    public DriverPreferencesResponse getPreferences(String email) {
        String driverId = driverService.getEntityByEmail(email).getId();
        return preferences.computeIfAbsent(driverId, key -> defaultPreferences());
    }

    public DriverPreferencesResponse updatePreferences(String email, DriverPreferencesRequest request) {
        String driverId = driverService.getEntityByEmail(email).getId();
        DriverPreferencesResponse response = new DriverPreferencesResponse();
        response.setLanguage(request.getLanguage());
        response.setTimezone(request.getTimezone());
        response.setNotificationMethod(request.getNotificationMethod());
        preferences.put(driverId, response);
        return response;
    }

    private DriverPreferencesResponse defaultPreferences() {
        DriverPreferencesResponse preferences = new DriverPreferencesResponse();
        preferences.setLanguage("en");
        preferences.setTimezone("UTC");
        preferences.setNotificationMethod("EMAIL");
        return preferences;
    }
}
