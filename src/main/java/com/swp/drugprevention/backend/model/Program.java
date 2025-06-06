package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Programs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Program {
    @Id
    @Column(name = "ProgramID")
    private Integer programId;

    @Column(name = "Title", nullable = false, length = 255)
    private String title;

    @Column(name = "Date")
    private java.sql.Date date;

    @Column(name = "Location", length = 255)
    private String location;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Topic", length = 255)
    private String topic;

    @ManyToOne
    @JoinColumn(name = "ConsultantID", referencedColumnName = "ConsultantID")
    private Consultant consultant;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<Participation> participations;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<DashboardReport> reports;
}