package com.etrichmond.driver_management.document;

import java.time.LocalDate;

import com.etrichmond.driver_management.common.BaseEntity;
import com.etrichmond.driver_management.driver.DriverEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "driver_documents")
@Getter
@Setter
public class DocumentEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id")
    private DriverEntity driver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    private LocalDate verifiedAt;

    private String verifiedBy;

    private String rejectionReason;

    private String remarks;
}