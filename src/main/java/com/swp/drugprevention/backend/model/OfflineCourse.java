package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfflineCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    @ManyToOne
    @JoinColumn(name = "ConsultantID", nullable = false)
    private Consultant consultant;

    @ManyToMany(mappedBy = "registeredCourses")
    private List<User> registeredUsers;

    @OneToMany(mappedBy = "offlineCourse", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course")
    private List<Payment> payments;

    @Column(nullable = false)
    private Boolean active = true;


    private double price;

    private String location;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int maxCapacity;

}
