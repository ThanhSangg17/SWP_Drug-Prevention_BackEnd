package com.swp.drugprevention.backend.io.response;


import com.swp.drugprevention.backend.enums.AuthenticationProvider;
import com.swp.drugprevention.backend.enums.RoleName;
import com.swp.drugprevention.backend.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    @NotBlank(message = "Full Name is not blank")
    private String fullName;

    @NotBlank(message = "Email is not blank")
    @Email(message = "Email is not formatted correctly!!")
    private String email;

/*    @NotBlank(message = "Password is not blank")
    @Size(min = 8, message = "Password must be more than 8 characters") không nên gửi password lên client
    private String password;*/

    @NotNull(message = "Year of Birth is required") // Thay @NotBlank bằng @NotNull cho Integer
    @Min(value = 1900, message = "Year of Birth must be after 1900")
    private Integer yob;

    @NotBlank(message = "Gender is not blank")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    private String gender;

    @NotBlank(message = "Phone is not blank")
    @Pattern(regexp = "\\d{10}|\\d{11}", message = "Invalid phone number format. Must be 10 or 11 digits.")
    private String phone;

    private RoleName roleName;
    private AuthenticationProvider authenticationProvider;

    public static ProfileResponse fromEntity(User user) {
        return ProfileResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .yob(user.getYob())
                .roleName(user.getRoleName())
                .authenticationProvider(user.getAuthProvider())
                .build();
    }
}

