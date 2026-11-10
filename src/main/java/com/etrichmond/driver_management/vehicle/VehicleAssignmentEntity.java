package com.etrichmond.driver_management.vehicle;

import java.time.LocalDate;

import com.etrichmond.driver_management.common.BaseEntity;
import com.etrichmond.driver_management.driver.DriverEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicle_assignments")
@Getter
@Setter
public class VehicleAssignmentEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private DriverEntity driver;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @Column(nullable = false)
    private LocalDate assignmentDate;

    private LocalDate releasedDate;

}