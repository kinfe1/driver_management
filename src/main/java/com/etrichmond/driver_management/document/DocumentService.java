package com.etrichmond.driver_management.document;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentResponse upload(DocumentRequest request) {
        DocumentEntity entity = new DocumentEntity();
        entity.setDriverId(request.getDriverId());
        entity.setDocumentType(request.getDocumentType());
        entity.setFileName(request.getFileName());
        entity.setFileUrl(request.getFileUrl());
        entity.setExpiryDate(request.getExpiryDate());

        DocumentEntity saved = documentRepository.save(entity);
        return toResponse(saved);
    }

    public List<DocumentResponse> findByDriverId(String driverId) {
        return documentRepository.findByDriverId(driverId).stream().map(this::toResponse).toList();
    }

    private DocumentResponse toResponse(DocumentEntity entity) {
        return new DocumentResponse(entity.getId(), entity.getDriverId(), entity.getDocumentType(),
                entity.getFileName(), entity.getFileUrl(), entity.getExpiryDate());
    }
}
