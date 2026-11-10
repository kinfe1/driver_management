package com.etrichmond.driver_management.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etrichmond.driver_management.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> stats() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getStats(),
                "Dashboard stats fetched successfully"));
    }

    @GetMapping("/compliance-overview")
    public ResponseEntity<ApiResponse<ComplianceOverviewResponse>> complianceOverview() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getComplianceOverview(),
                "Compliance overview fetched successfully"));
    }

    @GetMapping("/recent-events")
    public ResponseEntity<ApiResponse<List<RecentEventResponse>>> recentEvents() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getRecentEvents(),
                "Recent events fetched successfully"));
    }
}
