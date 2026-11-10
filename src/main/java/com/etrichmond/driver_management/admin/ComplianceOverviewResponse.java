package com.etrichmond.driver_management.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceOverviewResponse {

    private int totalDocuments;
    private int validDocumentsCount;
    private int expiringSoonCount;
    private int expiredCount;
    private int validDocumentsPercent;
    private int expiringSoonPercent;
    private int expiredPercent;
}