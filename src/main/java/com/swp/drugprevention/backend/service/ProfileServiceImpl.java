package com.swp.drugprevention.backend.repository;

import com.swp.drugprevention.backend.io.ProfileRequest;
import com.swp.drugprevention.backend.io.ProfileResponse;
import com.swp.drugprevention.backend.enums.AuthenticationProvider;
import com.swp.drugprevention.backend.enums.RoleName;
import com.swp.drugprevention.backend.io.request.PendingRegistration;
import com.swp.drugprevention.backend.io.request.ProfileRequest;
import com.swp.drugprevention.backend.io.response.ProfileResponse;
import com.swp.drugprevention.backend.model.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest request);

    ProfileResponse getProfile(String email);

    void sendResetOtp(String email);

    void resetPassword(String email, String otp, String newPassword);

    void sendOtp(ProfileRequest request);

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


    @Override
    public void sendOtp(ProfileRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists.");
        }

        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        long expiryTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 phút

        PendingRegistration pending = new PendingRegistration();
        pending.setProfileRequest(request);
        pending.setOtp(otp);
        pending.setExpiryTime(expiryTime);

        pendingRegistrations.put(request.getEmail(), pending);

        try {
            emailService.sendOtpEmail(request.getEmail(), otp);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    public void verifyOtp(String otp) {
        // tìm OTP trùng trong pendingRegistrations
        Optional<Map.Entry<String, PendingRegistration>> match = pendingRegistrations.entrySet().stream()
                .filter(entry -> entry.getValue().getOtp().equals(otp))
                .findFirst();

        if (match.isEmpty()) {
            throw new RuntimeException("Invalid OTP");
        }

        PendingRegistration pending = match.get().getValue();

        if (pending.getExpiryTime() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP expired");
        }

        // tạo account chính thức
        ProfileRequest request = pending.getProfileRequest();
        createProfile(request);

        // xóa khỏi bộ nhớ tạm
        pendingRegistrations.remove(request.getEmail());
    }




    private ProfileResponse convertToProfileResponse(User newProfile) {
        return ProfileResponse.builder()
                .fullName(newProfile.getFullName())
                .email(newProfile.getEmail())
                .yob(newProfile.getYob())
                .gender(newProfile.getGender())
                .phone(newProfile.getPhone())
                .roleName(newProfile.getRoleName())
                .authenticationProvider(newProfile.getAuthProvider())
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
                .resetOtpExpireAt(0L)
                .verifyOTP(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .authProvider(AuthenticationProvider.LOCAL)
                .roleName(RoleName.USER) // Gán roleName mặc định là USER
                .build();


    }


    public User loginRegisterByGoogleOAuth2(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info("USER Email from GOOGLE IS {}",email);
        log.info("USER Name from GOOGLE IS {}",name);

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setFullName(name);
            user.setEmail(email);
            user.setAuthProvider(AuthenticationProvider.GOOGLE);
            user.setRoleName(RoleName.USER); // Gán roleName mặc định là USER khi đăng nhập với Google
            return userRepository.save(user);
        }
        return user;
    }

    @Override
    public List<ProfileResponse> getAllUserProfile() {
        // Lấy tất cả user và ánh xạ thành danh sách ProfileResponse
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToProfileResponse)
                .collect(Collectors.toList());
    }
}
