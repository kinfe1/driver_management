package com.etrichmond.driver_management.document;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentRequest {

    @NotBlank
    private String driverId;

    @NotBlank
    private String documentType;

    @NotBlank
    private String fileName;

    @NotBlank
    private String fileUrl;

    private String expiryDate;
}
