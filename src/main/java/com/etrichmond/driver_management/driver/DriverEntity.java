package com.etrichmond.driver_management.driver;

import java.time.LocalDate;

import com.etrichmond.driver_management.auth.UserEntity;
import com.etrichmond.driver_management.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "drivers", indexes = {
        @Index(name = "idx_driver_license", columnList = "license_number"),
        @Index(name = "idx_driver_employee", columnList = "employee_id"),
        @Index(name = "idx_driver_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(length = 50)
    private String middleName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(length = 20)
    private String gender;

    private LocalDate dateOfBirth;

    @Column(length = 50)
    private String nationality;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 20)
    private String secondaryPhone;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, unique = true, length = 50)
    private String employeeId;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private String driverType;

    @Column(nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(nullable = false)
    private LocalDate licenseExpiry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DriverStatus status = DriverStatus.PENDING_APPROVAL;

    private String photoUrl;

}