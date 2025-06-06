package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @Column(name = "AppointmentID")
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ConsultantID", referencedColumnName = "ConsultantID")
    private Consultant consultant;

    @Column(name = "Date")
    private java.sql.Date date;

    @Column(name = "Time")
    private java.sql.Time time;

    @Column(name = "Status", length = 50)
    private String status;

    @Column(name = "Location", length = 255)
    private String location;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<Payment> payments;
}