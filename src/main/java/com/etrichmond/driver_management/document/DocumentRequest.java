package com.etrichmond.driver_management.document;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentRequest {

    @NotBlank
    private String driverId;

    @NotNull
    private DocumentType documentType;

    @NotBlank
    private String originalFileName;

    @NotBlank
    private String storedFileName;

    @NotBlank
    private String fileUrl;

    @NotBlank
    private String contentType;

    @NotNull
    private Long fileSize;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String remarks;
}