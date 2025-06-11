package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "UserID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    private String verifyOTP;

    private Boolean isAccountVerified;

    private Long verifyOtpExpireAt;

    private String resetOtp;

    private Long resetOtpExpireAt;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name = "YOB")
    private Integer yob;

    @Column(name = "Gender", length = 10)
    private String gender;

    @Column(name = "Phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authProvider;



    public User (String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    @ManyToOne
    @JoinColumn(name = "RoleID", referencedColumnName = "RoleID")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Survey> surveys;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DashBoardSurvey> dashBoardSurveys;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Participation> participation;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<DashboardReport> reports;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;


}