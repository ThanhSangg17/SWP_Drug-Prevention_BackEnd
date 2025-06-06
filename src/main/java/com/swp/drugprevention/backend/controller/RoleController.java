package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.model.Role;
import com.swp.drugprevention.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/getAllRoles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping(value = "/saveRole")
    public Role createRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }
}
