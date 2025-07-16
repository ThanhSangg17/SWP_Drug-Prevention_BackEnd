package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfflineCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @Column(name = "courseName")
    private String tenKhoaHoc;

    @ManyToOne
    @JoinColumn(name = "ConsultantID", nullable = false)
    private Consultant consultant;

    @ManyToMany(mappedBy = "registeredCourses")
    private List<User> registeredUsers;

    @OneToMany(mappedBy = "offlineCourse", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course")
    private List<Payment> payments;



    private double giaTien;
    private String diaDiem;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private int soLuongToiDa;
}
