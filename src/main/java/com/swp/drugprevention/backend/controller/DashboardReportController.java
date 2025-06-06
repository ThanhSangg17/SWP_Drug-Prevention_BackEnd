package com.swp.drugprevention.backend.controller;
import com.swp.drugprevention.backend.model.DashboardReport;
import com.swp.drugprevention.backend.service.DashboardReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard-reports")
public class DashboardReportController {
    @Autowired
    private DashboardReportService dashboardReportService;

    @GetMapping("/getAllReports")
    public List<DashboardReport> getAllReports() {
        return dashboardReportService.getAllReports();
    }

    @PostMapping("/createReport")
    public DashboardReport createReport(@RequestBody DashboardReport dashboardReport) {
        return dashboardReportService.saveReport(dashboardReport);
    }
}
