package com.etrichmond.driver_management.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etrichmond.driver_management.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService reportService;

    @GetMapping("/export")
    public ResponseEntity<ApiResponse<String>> export() {
        return ResponseEntity.ok(ApiResponse.success(reportService.export(), "Report export triggered successfully"));
    }
}
