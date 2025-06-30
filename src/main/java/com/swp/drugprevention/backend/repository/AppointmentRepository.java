package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Appointment;
import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByConsultantAndDate(Consultant consultant, LocalDate date);
    List<Appointment> findByUserAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(User user, LocalDate currentDate);

}
