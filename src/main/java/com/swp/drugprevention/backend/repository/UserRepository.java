package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByUserId(Integer id);
    Optional<User> findByRoleName(String roleName);
}
