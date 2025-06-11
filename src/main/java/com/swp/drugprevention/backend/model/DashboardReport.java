package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DashboardReports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardReport {
    @Id
    @Column(name = "ReportID")
    private Integer reportId;

    @Column(name = "Title", nullable = false, length = 255)
    private String title;

    @Column(name = "CreatedDate", nullable = false)
    private java.sql.Date createdDate;

    @Column(name = "Metrics", columnDefinition = "TEXT")
    private String metrics;

    @ManyToOne
    @JoinColumn(name = "CreatedBy", referencedColumnName = "UserID")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "ProgramID", referencedColumnName = "ProgramID")
    private Program program;
}