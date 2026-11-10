package com.etrichmond.driver_management.document;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {

    private String id;

    private String driverId;

    private String driverName;

    private DocumentType documentType;

    private String originalFileName;

    private String storedFileName;

    private String fileUrl;

    private String contentType;

    private Long fileSize;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private DocumentStatus status;

    private LocalDate verifiedAt;

    private String verifiedBy;

    private String rejectionReason;

    private String remarks;
}