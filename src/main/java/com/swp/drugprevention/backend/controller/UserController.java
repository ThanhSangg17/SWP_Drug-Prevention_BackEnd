package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(value = "/createUser")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
