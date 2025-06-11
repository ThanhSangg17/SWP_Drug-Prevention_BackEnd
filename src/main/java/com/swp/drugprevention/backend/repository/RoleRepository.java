package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.model.Role;
import com.swp.drugprevention.backend.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
}
