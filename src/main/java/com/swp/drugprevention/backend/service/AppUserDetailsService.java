package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting to load user by email: " + email);
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("User not found for email: " + email);
                    return new UsernameNotFoundException("Email not found for the email: " + email);
                });
        String role = "ROLE_" + existingUser.getRoleName().name();
        System.out.println("User found: " + existingUser.getEmail() + ", Role: " + role);
        return new org.springframework.security.core.userdetails.User(
                existingUser.getEmail(),
                existingUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}
