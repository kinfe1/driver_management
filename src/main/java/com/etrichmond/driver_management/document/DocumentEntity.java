package com.etrichmond.driver_management.document;

import com.etrichmond.driver_management.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "driver_documents")
@Getter
@Setter
public class DocumentEntity extends BaseEntity {

    @Column(nullable = false)
    private String driverId;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    private String expiryDate;
}
