package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

}
