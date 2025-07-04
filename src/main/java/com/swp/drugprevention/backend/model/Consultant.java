package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private User user;

    @Column(name = "Specialization", length = 255)
    private String specialization;

    @Column(name = "Availability")
    private boolean availability;

    @Column(name = "Schedule", length = 255)
    private String schedule;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Course> courses;

    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL)
    private List<Program> programs;

    @Column(name = "Phone", length = 20)
    private String phone;

    @Column(name = "YOB")
    private Integer yob;
}