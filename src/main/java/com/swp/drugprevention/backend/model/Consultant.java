package com.swp.drugprevention.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Consultants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ConsultantID")
    private Integer consultantId;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    @OneToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", nullable = false, unique = true)
    @JsonIgnore // 👈 tránh vòng lặp nếu User lại có Consultant
    private User user;

    @Column(name = "Specialization", length = 255)
    private String specialization;

    @Column(name = "Availability")
    private boolean availability;

    @Column(name = "Schedule", length = 255)
    private String schedule;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;

    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Program> programs;

    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OfflineCourse> offlineCourses; // 👈 Thêm dòng này nếu anh có

    @Column(name = "Phone", length = 20)
    private String phone;

    @Column(name = "YOB")
    private Integer yob;
}
