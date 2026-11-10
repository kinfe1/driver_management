package com.etrichmond.driver_management.admin;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.etrichmond.driver_management.document.DocumentEntity;
import com.etrichmond.driver_management.document.DocumentRepository;
import com.etrichmond.driver_management.driver.DriverEntity;
import com.etrichmond.driver_management.driver.DriverRepository;
import com.etrichmond.driver_management.driver.DriverStatus;
import com.etrichmond.driver_management.vehicle.VehicleAssignmentEntity;
import com.etrichmond.driver_management.vehicle.VehicleAssignmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final DriverRepository driverRepository;
    private final VehicleAssignmentRepository vehicleAssignmentRepository;
    private final DocumentRepository documentRepository;

    public DashboardStatsResponse getStats() {

        DashboardStatsResponse response = new DashboardStatsResponse();

        long totalDrivers = driverRepository.count();

        long activeDrivers = driverRepository.countByStatus(DriverStatus.ACTIVE);

        long suspendedDrivers = driverRepository.countByStatus(DriverStatus.SUSPENDED);

        long expiredLicenses = driverRepository.countExpiredLicenses();

        response.setTotalDrivers((int) totalDrivers);
        response.setActiveDrivers((int) activeDrivers);
        response.setSuspendedDrivers((int) suspendedDrivers);
        response.setExpiredLicenses((int) expiredLicenses);

        return response;
    }

    public ComplianceOverviewResponse getComplianceOverview() {

        List<DocumentEntity> documents = documentRepository.findAll();

        LocalDate today = LocalDate.now();
        LocalDate next30Days = today.plusDays(30);

        int totalDocuments = documents.size();

        int expired = (int) documents.stream()
                .filter(document -> document.getExpiryDate() != null
                        && document.getExpiryDate().isBefore(today))
                .count();

        int expiringSoon = (int) documents.stream()
                .filter(document -> document.getExpiryDate() != null
                        && !document.getExpiryDate().isBefore(today)
                        && !document.getExpiryDate().isAfter(next30Days))
                .count();

        int valid = totalDocuments - expired - expiringSoon;

        int validPercent = totalDocuments == 0
                ? 0
                : (int) Math.round((valid * 100.0) / totalDocuments);

        int expiringSoonPercent = totalDocuments == 0
                ? 0
                : (int) Math.round((expiringSoon * 100.0) / totalDocuments);

        int expiredPercent = totalDocuments == 0
                ? 0
                : (int) Math.round((expired * 100.0) / totalDocuments);

        return new ComplianceOverviewResponse(
                totalDocuments,
                valid,
                expiringSoon,
                expired,
                validPercent,
                expiringSoonPercent,
                expiredPercent);
    }

    public List<RecentEventResponse> getRecentEvents() {

        List<RecentEventResponse> events = new ArrayList<>();

        // Driver registrations / status changes
        List<DriverEntity> drivers = driverRepository.findAll();

        for (DriverEntity driver : drivers) {

            String status = driver.getStatus().name();

            events.add(
                    new RecentEventResponse(
                            "Driver registered",
                            driver.getFirstName() + " " + driver.getLastName(),
                            formatTime(driver.getCreatedAt()),
                            status));

            if (driver.getStatus().name().equals("ACTIVE")) {

                events.add(
                        new RecentEventResponse(
                                "Driver approved",
                                driver.getFirstName() + " " + driver.getLastName(),
                                formatTime(driver.getUpdatedAt()),
                                "ACTIVE"));
            }
        }

        // Vehicle assignments
        List<VehicleAssignmentEntity> assignments = vehicleAssignmentRepository.findAll();

        for (VehicleAssignmentEntity assignment : assignments) {

            String driverName = assignment.getDriver().getFirstName()
                    + " "
                    + assignment.getDriver().getLastName();

            events.add(
                    new RecentEventResponse(
                            "Vehicle assigned",
                            driverName,
                            formatTime(
                                    assignment.getCreatedAt()),
                            "ASSIGNED"));
        }

        // newest first
        return events.stream()
                .sorted(
                        Comparator.comparing(
                                RecentEventResponse::getTime).reversed())
                .limit(10)
                .toList();
    }

    private String formatTime(LocalDateTime dateTime) {

        if (dateTime == null) {
            return "Unknown";
        }

        Duration duration = Duration.between(dateTime, LocalDateTime.now());

        long minutes = duration.toMinutes();

        if (minutes < 60) {
            return minutes + " minutes ago";
        }

        long hours = duration.toHours();

        if (hours < 24) {
            return hours + " hours ago";
        }

        long days = duration.toDays();

        return days + " days ago";
    }

}