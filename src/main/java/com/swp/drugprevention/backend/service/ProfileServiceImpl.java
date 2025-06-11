package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.io.ProfileRequest;
import com.swp.drugprevention.backend.io.ProfileResponse;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.repository.ProfileService;
import com.swp.drugprevention.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        User newProfile = convertToUserEntity(request);
        //Vấn đề có lẽ nằm ở loại if nào/test và thấy không nằm ở if. vậy 401 nó bị chặn chỗ nào?
        //lúc lỗi lúc không
        if (!userRepository.existsByEmail(request.getEmail())){
            newProfile=userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }

        /*if (!userRepository.existsByEmail(request.getEmail())) {
            newProfile = userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }*/

        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        //Fuck this, Cái đb này làm t ngồi fix mấy tiếng =((

        /*newProfile = userRepository.save(newProfile);
        return convertToProfileResponse(newProfile);*/

    }

    @Override
    public ProfileResponse getProfile(String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " +email));
        return convertToProfileResponse(existingUser);
    }

    @Override
    public void sendResetOtp(String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: "+email));

        //Generate 6 digit otp
       String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

       //calculate expiry time (current time + 15 minutes in milliseconds) (time hết hạn)
        long expiryTime = System.currentTimeMillis() + (15 * 60 * 1000);

        //update the profile/user
        existingUser.setResetOtp(otp);
        existingUser.setResetOtpExpireAt(expiryTime);

        //save into the database
        userRepository.save(existingUser);

        try {
            emailService.sendResetOtpEmail(existingUser.getEmail(), otp);
        }catch (Exception e) {
            throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: "+email));

        if (existingUser.getResetOtp() == null || !existingUser.getResetOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (existingUser.getResetOtpExpireAt() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP Expired");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpireAt(0L);

        userRepository.save(existingUser);
    }

    private ProfileResponse convertToProfileResponse(User newProfile) {
        return ProfileResponse.builder()
                .fullName(newProfile.getFullName())
                .email(newProfile.getEmail())
                .yob(newProfile.getYob())
                .gender(newProfile.getGender())
                .phone(newProfile.getPhone())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .build();
    }

    private User convertToUserEntity(ProfileRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .yob(request.getYob())
                .gender(request.getGender())
                .phone(request.getPhone())
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOTP(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();


    }
}
