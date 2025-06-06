package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.model.DashboardReport;
import com.swp.drugprevention.backend.repository.DashboardReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardReportService {
    @Autowired
    private DashboardReportRepository dashboardReportRepository;

    public List<DashboardReport> getAllReports() {
        return dashboardReportRepository.findAll();
    }

    public DashboardReport saveReport(DashboardReport report) {
        return dashboardReportRepository.save(report);
    }
}
