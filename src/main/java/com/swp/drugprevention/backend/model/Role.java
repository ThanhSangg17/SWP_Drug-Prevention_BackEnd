package com.swp.drugprevention.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Roles")
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @Column(name = "RoleID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "RoleName", nullable = true, length = 50)
    private RoleName roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;
}