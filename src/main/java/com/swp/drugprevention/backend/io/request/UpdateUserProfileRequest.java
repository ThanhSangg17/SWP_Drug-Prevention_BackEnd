package com.swp.drugprevention.backend.io.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserProfileRequest {
    private String fullName;
    @Min(value = 1900, message = "Year of Birth must be after 1900")
    @Max(value = 2030, message = "Year of Birth not be in the future")
    private Integer yob;
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    private String gender;
    @Pattern(regexp = "\\d{10}|\\d{11}", message = "Invalid phone number format. Must be 10 or 11 digits.")
    private String phone;
}
