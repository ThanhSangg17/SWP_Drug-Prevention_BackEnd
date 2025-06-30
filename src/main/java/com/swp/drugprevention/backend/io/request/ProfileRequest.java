package com.swp.drugprevention.backend.io.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProfileRequest {
    @NotBlank(message = "Full Name is not blank")
    private String fullName;

    @NotBlank(message = "Email is not blank")
    @Email(message = "Email is not formatted correctly!!")
    private String email;

    @NotBlank(message = "Password is not blank")
    @Size(min = 8, message = "Password must be more than 8 characters")
    private String password;

    @NotNull(message = "Year of Birth is required") // Thay @NotBlank bằng @NotNull cho Integer
    @Min(value = 1900, message = "Year of Birth must be after 1900")
    @Max(value = 2030, message = "Year of Birth not be in the future")
    private Integer yob;

    @NotBlank(message = "Gender is not blank")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    private String gender;

    @NotBlank(message = "Phone is not blank")
    @Pattern(regexp = "\\d{10}|\\d{11}", message = "Invalid phone number format. Must be 10 or 11 digits.")
    private String phone;
}