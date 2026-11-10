package com.etrichmond.driver_management.admin;

import org.springframework.stereotype.Service;

@Service
public class AdminReportService {

    public String export() {
        return "reports/operational-report.csv";
    }
}
