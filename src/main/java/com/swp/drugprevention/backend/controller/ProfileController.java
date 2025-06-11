package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.ProfileRequest;
import com.swp.drugprevention.backend.io.ProfileResponse;
import com.swp.drugprevention.backend.repository.ProfileService;
import com.swp.drugprevention.backend.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final EmailService emailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse response = profileService.createProfile(request);
        emailService.sendWelcomeEmail(response.getEmail(), response.getFullName());//dùng để gửi mail ngay sau khi đăng kí xong
        return response;
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) { //@CurrentSecurityContext(expression = "authentication?.name") dùng cho chức năng cần được xác thực và
        //nếu không được xác thực thì sẽ bị ném ra ngoại lệ được thiết lập trong CustomAuthenticationEntryPoint và cấu hình trong config
        return profileService.getProfile(email);
    }



}
