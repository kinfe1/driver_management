package com.etrichmond.driver_management.document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentResponse {
    private String id;
    private String driverId;
    private String documentType;
    private String fileName;
    private String fileUrl;
    private String expiryDate;
}
