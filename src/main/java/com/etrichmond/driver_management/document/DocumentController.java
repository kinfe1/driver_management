package com.etrichmond.driver_management.document;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etrichmond.driver_management.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentResponse>> upload(@Valid @RequestBody DocumentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(documentService.upload(request), "Document uploaded successfully"));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> findByDriverId(@PathVariable String driverId) {
        return ResponseEntity
                .ok(ApiResponse.success(documentService.findByDriverId(driverId), "Documents fetched successfully"));
    }
}
