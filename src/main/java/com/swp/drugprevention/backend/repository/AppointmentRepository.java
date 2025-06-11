package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
}
