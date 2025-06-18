package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.io.request.ProfileRequest;
import com.swp.drugprevention.backend.io.response.ProfileResponse;
import com.swp.drugprevention.backend.model.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.List;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);

    ProfileResponse getProfile(String email);

    void sendResetOtp(String email);

    void resetPassword(String email, String otp, String newPassword);

    void sendOtp(ProfileRequest request);

    void verifyOtp(String otp);

    User loginRegisterByGoogleOAuth2(OAuth2AuthenticationToken oAuth2AuthenticationToken);

    List<ProfileResponse> getAllUserProfile();
}
