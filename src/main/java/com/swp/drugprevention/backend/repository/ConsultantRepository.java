package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Integer> {
    boolean existsByUser(User user);
    void deleteByUser(User user);
    Optional<Consultant> findByUser(User user);
    Optional<Consultant> findByEmail(String email);
}
