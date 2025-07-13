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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // cần dòng này!
    @Column(name = "EnrollmentID")
    private Integer enrollmentId;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "OfflineCourseID", referencedColumnName = "id") // 👈 chú ý tên cột ID là "id"
    private OfflineCourse offlineCourse;

    @Column(name = "EnrollDate")
    private java.sql.Date enrollDate;

    @Column(name = "is_present")
    private Boolean isPresent = false;

}

