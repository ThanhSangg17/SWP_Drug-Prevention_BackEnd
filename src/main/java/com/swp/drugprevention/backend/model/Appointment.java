package com.swp.drugprevention.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Entity
@Table(name = "Appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AppointmentID")
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ConsultantID", referencedColumnName = "ConsultantID")
    @JsonBackReference
    private Consultant consultant;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "StartTime")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "EndTime")
    private LocalTime endTime;

    @Column(name = "Status", length = 50)
    private String status;

    @Column(name = "Location", length = 255)
    private String location;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Payment> payments;

}