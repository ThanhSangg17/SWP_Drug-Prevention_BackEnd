package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.enums.ConsultationStatus;
import com.swp.drugprevention.backend.model.Appointment;
import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByConsultantAndDate(Consultant consultant, LocalDate date);
    List<Appointment> findByUserAndDateGreaterThanEqualOrderByDateAscStartTimeAsc(User user, LocalDate currentDate);
    List<Appointment> findByUserUserIdAndDate(Integer userId, LocalDate date); //Tên phải đặt đúng quy tắt
    // Tên phương thức phải tuân theo quy tắc đặt tên của Spring Data JPA để tự động tạo truy vấn
    // Quy tắt là: findBy + TênTrường + TênPhươngThức
    // Ví dụ: findByUserUserIdAndDate sẽ tạo truy vấn tìm kiếm theo UserID và Date
    List<Appointment> findByStatusNot(ConsultationStatus status);
}
