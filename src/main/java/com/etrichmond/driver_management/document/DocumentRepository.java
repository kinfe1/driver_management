package com.etrichmond.driver_management.document;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, String> {

    List<DocumentEntity> findByDriverId(String driverId);

    List<DocumentEntity> findByStatus(DocumentStatus status);
}