package com.etrichmond.driver_management.driver;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DriverRepository extends JpaRepository<DriverEntity, String> {

    Optional<DriverEntity> findByUserEmail(String email);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByEmployeeId(String employeeId);

    long countByStatus(DriverStatus status);

    @Query("""
                SELECT COUNT(d)
                FROM DriverEntity d
                WHERE d.licenseExpiry < CURRENT_DATE
            """)
    long countExpiredLicenses();

}