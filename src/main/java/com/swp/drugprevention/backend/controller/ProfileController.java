package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.enums.RoleName;
import com.swp.drugprevention.backend.exception.EmailAlreadyExistsException;
import com.swp.drugprevention.backend.exception.SameRoleException;
import com.swp.drugprevention.backend.io.request.ProfileRequest;
import com.swp.drugprevention.backend.io.request.RoleUpdateRequest;
import com.swp.drugprevention.backend.io.response.ProfileResponse;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.repository.ProfileService;
import com.swp.drugprevention.backend.repository.UserRepository;
import com.swp.drugprevention.backend.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfileResponse> updateUserRoles(@PathVariable Integer userId, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("UserID is not found: " + userId));

        RoleName newRoleName = roleUpdateRequest.getRoleName();
        RoleName currentRoleName = user.getRoleName();

        if (newRoleName == currentRoleName) {
            throw new SameRoleException("The new role '" + newRoleName + "' is the same as the current role.");
        }

        user.setRoleName(newRoleName); // Cập nhật roleName trong User
        User updatedUser = userRepository.save(user);

        ProfileResponse profileResponse = profileService.getProfile(updatedUser.getEmail());
        return ResponseEntity.ok()
                .header("X-Custom-Message", "User role updated successfully for user ID: " + userId)
                .body(profileResponse);
    }
    @GetMapping("/profileAllUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfileResponse>> getAllUserProfile() {
        List<ProfileResponse> allProfiles = profileService.getAllUserProfile();
        return ResponseEntity.ok(allProfiles);
    }
}
