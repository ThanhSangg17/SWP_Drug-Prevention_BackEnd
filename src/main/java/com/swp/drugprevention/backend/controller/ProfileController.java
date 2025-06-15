package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.exception.EmailAlreadyExistsException;
import com.swp.drugprevention.backend.io.ProfileRequest;
import com.swp.drugprevention.backend.io.ProfileResponse;
import com.swp.drugprevention.backend.repository.ProfileService;
import com.swp.drugprevention.backend.repository.UserRepository;
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
    private final UserRepository userRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@Valid @RequestBody ProfileRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> { throw new EmailAlreadyExistsException("Email is exist: "+request.getEmail());
                });
        //nếu không tồn tại thì tạo mới
        /*ProfileResponse response = profileService.createProfile(request);
        emailService.sendWelcomeEmail(response.getEmail(), response.getFullName());//dùng để gửi mail ngay sau khi đăng kí xong
        return response;*/
        profileService.sendOtp(request);
        return ResponseEntity.ok("OTP sent to " + request.getEmail());
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) { //@CurrentSecurityContext(expression = "authentication?.name") dùng cho chức năng cần được xác thực và
        //nếu không được xác thực thì sẽ bị ném ra ngoại lệ được thiết lập trong CustomAuthenticationEntryPoint và cấu hình trong config
        return profileService.getProfile(email);
    }


}
