package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Enrollments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {
    @Id
    @Column(name = "EnrollmentID")
    private Integer enrollmentId;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "CourseID", referencedColumnName = "CourseID")
    private Course course;

    @Column(name = "EnrollDate")
    private java.sql.Date enrollDate;
}