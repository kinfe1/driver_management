package com.etrichmond.driver_management.document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.etrichmond.driver_management.common.exception.ResourceNotFoundException;
import com.etrichmond.driver_management.driver.DriverEntity;
import com.etrichmond.driver_management.driver.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DriverRepository driverRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public List<DocumentResponse> findAll() {
        return documentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DocumentResponse upload(
            String driverId,
            DocumentType documentType,
            LocalDate issueDate,
            LocalDate expiryDate,
            String remarks,
            MultipartFile file) {

        DriverEntity driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String storedFileName = UUID.randomUUID()
                    + "_"
                    + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            DocumentEntity entity = new DocumentEntity();

            entity.setDriver(driver);
            entity.setDocumentType(documentType);

            entity.setOriginalFileName(file.getOriginalFilename());
            entity.setStoredFileName(storedFileName);
            entity.setFileUrl("/uploads/" + storedFileName);

            entity.setContentType(file.getContentType());
            entity.setFileSize(file.getSize());

            entity.setIssueDate(issueDate);
            entity.setExpiryDate(expiryDate);
            entity.setRemarks(remarks);

            entity.setStatus(DocumentStatus.PENDING);

            return toResponse(documentRepository.save(entity));

        } catch (IOException e) {
            throw new RuntimeException("Document upload failed", e);
        }
    }

    public DocumentResponse update(String documentId, DocumentRequest request) {

        DocumentEntity entity = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        entity.setDocumentType(request.getDocumentType());
        entity.setIssueDate(request.getIssueDate());
        entity.setExpiryDate(request.getExpiryDate());
        entity.setRemarks(request.getRemarks());

        entity.setStatus(DocumentStatus.PENDING);
        entity.setVerifiedAt(null);
        entity.setVerifiedBy(null);
        entity.setRejectionReason(null);

        return toResponse(documentRepository.save(entity));
    }

    public void delete(String documentId) {

        DocumentEntity entity = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        documentRepository.delete(entity);
    }

    public DocumentResponse findById(String documentId) {

        return documentRepository.findById(documentId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    public List<DocumentResponse> findByDriverId(String driverId) {

        return documentRepository.findByDriverId(driverId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<DocumentResponse> findPending() {

        return documentRepository.findByStatus(DocumentStatus.PENDING)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<DocumentResponse> findVerified() {

        return documentRepository.findByStatus(DocumentStatus.VERIFIED)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<DocumentResponse> findExpired() {

        LocalDate today = LocalDate.now();

        documentRepository.findAll()
                .stream()
                .filter(d -> d.getExpiryDate() != null
                        && d.getExpiryDate().isBefore(today))
                .forEach(document -> {

                    if (document.getStatus() != DocumentStatus.EXPIRED) {
                        document.setStatus(DocumentStatus.EXPIRED);
                        documentRepository.save(document);
                    }

                });

        return documentRepository.findByStatus(DocumentStatus.EXPIRED)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<DocumentResponse> findExpiringSoon() {

        LocalDate today = LocalDate.now();
        LocalDate next30Days = today.plusDays(30);

        return documentRepository.findAll()
                .stream()
                .filter(d -> d.getExpiryDate() != null
                        && !d.getExpiryDate().isBefore(today)
                        && !d.getExpiryDate().isAfter(next30Days))
                .map(this::toResponse)
                .toList();
    }

    public DocumentResponse verify(String documentId) {

        DocumentEntity entity = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        entity.setStatus(DocumentStatus.VERIFIED);
        entity.setVerifiedAt(LocalDate.now());
        entity.setVerifiedBy("ADMIN");
        entity.setRejectionReason(null);

        return toResponse(documentRepository.save(entity));
    }

    public DocumentResponse reject(String documentId, String reason) {

        DocumentEntity entity = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        entity.setStatus(DocumentStatus.REJECTED);
        entity.setVerifiedAt(LocalDate.now());
        entity.setRejectionReason(reason);

        return toResponse(documentRepository.save(entity));
    }

    public String getDownloadUrl(String documentId) {

        DocumentEntity entity = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        return entity.getFileUrl();
    }

    private DocumentResponse toResponse(DocumentEntity entity) {

        return new DocumentResponse(
                entity.getId(),
                entity.getDriver().getId(),
                entity.getDriver().getFirstName()
                        + " "
                        + entity.getDriver().getLastName(),
                entity.getDocumentType(),
                entity.getOriginalFileName(),
                entity.getStoredFileName(),
                entity.getFileUrl(),
                entity.getContentType(),
                entity.getFileSize(),
                entity.getIssueDate(),
                entity.getExpiryDate(),
                entity.getStatus(),
                entity.getVerifiedAt(),
                entity.getVerifiedBy(),
                entity.getRejectionReason(),
                entity.getRemarks());
    }

    public DocumentResponse replace(
            String documentId,
            DocumentType documentType,
            LocalDate issueDate,
            LocalDate expiryDate,
            String remarks,
            MultipartFile file) {

        DocumentEntity entity = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String storedFileName = UUID.randomUUID()
                    + "_"
                    + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            entity.setDocumentType(documentType);

            entity.setOriginalFileName(file.getOriginalFilename());
            entity.setStoredFileName(storedFileName);

            entity.setFileUrl("/uploads/" + storedFileName);

            entity.setContentType(file.getContentType());
            entity.setFileSize(file.getSize());

            entity.setIssueDate(issueDate);
            entity.setExpiryDate(expiryDate);
            entity.setRemarks(remarks);

            entity.setStatus(DocumentStatus.PENDING);
            entity.setVerifiedAt(null);
            entity.setVerifiedBy(null);
            entity.setRejectionReason(null);

            return toResponse(documentRepository.save(entity));

        } catch (IOException e) {
            throw new RuntimeException("Document replacement failed", e);
        }
    }

    public byte[] loadFile(String documentId) {

        DocumentEntity entity = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        try {

            Path path = Paths.get(uploadDir)
                    .resolve(entity.getStoredFileName());

            return Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Unable to load document", e);
        }
    }

    public Resource load(String documentId) {

        DocumentEntity entity = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        try {

            Path filePath = Paths.get(uploadDir)
                    .resolve(entity.getStoredFileName())
                    .normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File not found");
            }

            return resource;

        } catch (IOException e) {
            throw new RuntimeException("Could not load file", e);
        }
    }
}