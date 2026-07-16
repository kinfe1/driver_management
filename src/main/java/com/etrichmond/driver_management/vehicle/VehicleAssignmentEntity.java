package com.etrichmond.driver_management.vehicle;

import com.etrichmond.driver_management.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicle_assignments")
@Getter
@Setter
public class VehicleAssignmentEntity extends BaseEntity {

    @Column(nullable = false)
    private String driverId;

    @Column(nullable = false)
    private String vehicleId;

    @Column(nullable = false)
    private String assignmentDate;
}
