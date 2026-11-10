package com.etrichmond.driver_management.document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.etrichmond.driver_management.common.exception.ResourceNotFoundException;
import com.etrichmond.driver_management.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/documents", "/api/v1/documents" })
@RequiredArgsConstructor
public class DocumentController {

        private final DocumentService documentService;

        @GetMapping
        public ResponseEntity<ApiResponse<List<DocumentResponse>>> findAll() {
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                documentService.findAll(),
                                                "All documents fetched successfully"));
        }

        @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<DocumentResponse>> upload(

                        @RequestParam String driverId,

                        @RequestParam DocumentType documentType,

                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,

                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,

                        @RequestParam(required = false) String remarks,

                        @RequestParam("file") MultipartFile file) {

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(
                                                documentService.upload(
                                                                driverId,
                                                                documentType,
                                                                issueDate,
                                                                expiryDate,
                                                                remarks,
                                                                file),
                                                "Document uploaded successfully"));
        }

        @PutMapping(value = "/{documentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<DocumentResponse>> replace(

                        @PathVariable String documentId,

                        @RequestParam DocumentType documentType,

                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,

                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,

                        @RequestParam(required = false) String remarks,

                        @RequestParam("file") MultipartFile file) {

                return ResponseEntity.ok(ApiResponse.success(
                                documentService.replace(
                                                documentId,
                                                documentType,
                                                issueDate,
                                                expiryDate,
                                                remarks,
                                                file),
                                "Document replaced successfully"));
        }

        @DeleteMapping("/{documentId}")
        public ResponseEntity<ApiResponse<String>> delete(
                        @PathVariable String documentId) {

                documentService.delete(documentId);

                return ResponseEntity.ok(ApiResponse.success(
                                documentId,
                                "Document deleted successfully"));
        }

        @GetMapping("/{documentId}")
        public ResponseEntity<ApiResponse<DocumentResponse>> findById(
                        @PathVariable String documentId) {

                return ResponseEntity.ok(ApiResponse.success(
                                documentService.findById(documentId),
                                "Document fetched successfully"));
        }

        @GetMapping("/driver/{driverId}")
        public ResponseEntity<ApiResponse<List<DocumentResponse>>> findByDriver(
                        @PathVariable String driverId) {

                return ResponseEntity.ok(ApiResponse.success(
                                documentService.findByDriverId(driverId),
                                "Driver documents fetched successfully"));
        }

        @GetMapping("/pending")
        public ResponseEntity<ApiResponse<List<DocumentResponse>>> pending() {

                return ResponseEntity.ok(ApiResponse.success(
                                documentService.findPending(),
                                "Pending documents fetched successfully"));
        }

        @GetMapping("/verified")
        public ResponseEntity<ApiResponse<List<DocumentResponse>>> verified() {

                return ResponseEntity.ok(ApiResponse.success(
                                documentService.findVerified(),
                                "Verified documents fetched successfully"));
        }

        @GetMapping("/expired")
        public ResponseEntity<ApiResponse<List<DocumentResponse>>> expired() {

                return ResponseEntity.ok(ApiResponse.success(
                                documentService.findExpired(),
                                "Expired documents fetched successfully"));
        }

        @GetMapping("/expiring")
        public ResponseEntity<ApiResponse<List<DocumentResponse>>> expiringSoon() {

                return ResponseEntity.ok(ApiResponse.success(
                                documentService.findExpiringSoon(),
                                "Documents expiring soon fetched successfully"));
        }

        @PostMapping("/{documentId}/verify")
        public ResponseEntity<ApiResponse<DocumentResponse>> verify(
                        @PathVariable String documentId) {

                return ResponseEntity.ok(ApiResponse.success(
                                documentService.verify(documentId),
                                "Document verified successfully"));
        }

        @PostMapping("/{documentId}/reject")
        public ResponseEntity<ApiResponse<DocumentResponse>> reject(
                        @PathVariable String documentId,
                        @RequestParam String reason) {

                return ResponseEntity.ok(ApiResponse.success(
                                documentService.reject(documentId, reason),
                                "Document rejected successfully"));
        }

        @GetMapping("/{documentId}/preview")
        public ResponseEntity<Resource> preview(
                        @PathVariable String documentId) {

                Resource resource = documentService.load(documentId);

                if (!resource.exists()) {
                        throw new ResourceNotFoundException("Document file not found");
                }

                MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

                try {
                        Path filePath = resource.getFile().toPath();
                        String contentType = Files.probeContentType(filePath);

                        if (contentType != null) {
                                mediaType = MediaType.parseMediaType(contentType);
                        }

                        return ResponseEntity.ok()
                                        .contentType(mediaType)
                                        .header(
                                                        HttpHeaders.CONTENT_DISPOSITION,
                                                        "inline; filename=\"" + filePath.getFileName().toString()
                                                                        + "\"")
                                        .body(resource);
                } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

        }

        @GetMapping("/{documentId}/download")
        public ResponseEntity<Resource> download(
                        @PathVariable String documentId) {

                Resource resource = documentService.load(documentId);

                String filename = documentService.findById(documentId)
                                .getOriginalFileName();

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" + filename + "\"")
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .body(resource);
        }
}