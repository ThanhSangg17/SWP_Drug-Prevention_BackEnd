package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.enums.ConsultationStatus;
import com.swp.drugprevention.backend.model.Appointment;
import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByConsultantAndDate(Consultant consultant, LocalDate date);
    List<Appointment> findByUserAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(User user, LocalDate currentDate);
    List<Appointment> findByUserUserIdAndDate(Integer userId, LocalDate date); //Tên phải đặt đúng quy tắt
    // Tên phương thức phải tuân theo quy tắc đặt tên của Spring Data JPA để tự động tạo truy vấn
    // Quy tắt là: findBy + TênTrường + TênPhươngThức
    // Ví dụ: findByUserUserIdAndDate sẽ tạo truy vấn tìm kiếm theo UserID và Date
    List<Appointment> findByStatusNot(ConsultationStatus status);
    List<Appointment> findByUserOrderByDateAscStartTimeAsc(User user);
    List<Appointment> findByConsultant(Consultant consultant);
    @Query("SELECT a FROM Appointment a WHERE " +
            "FUNCTION('TIMESTAMP', a.date, a.startTime) BETWEEN :start AND :end " +
            "AND a.status = 'Pending'")
    List<Appointment> findByDateTimeBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
    List<Appointment> findByStatus(ConsultationStatus status);
}
