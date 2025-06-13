package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.io.ProfileRequest;
import com.swp.drugprevention.backend.io.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);

    ProfileResponse getProfile(String email);

    void sendResetOtp(String email);

    void resetPassword(String email, String otp, String newPassword);

    void sendOtp(ProfileRequest request);

    void verifyOtp(String otp);

}
