package net.accel_tech.omni365_saas_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author NdourBamba18
 **/

@Data
public class ParticularFormRequest {

    @NotBlank(message = "First Name field is required!")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;

    @NotBlank(message = "Last Name field is required!")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;

    @NotBlank(message = "Personal Email field is required!")
    @Size(max = 180, message = "Email is too long")
    @jakarta.validation.constraints.Email(message = "Please, enter a valid email address")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format. Example: user@example.com")
    private String personalEmail;

    @NotBlank(message = "Phone Number field is required!")
    @Size(max = 20, message = "Phone number is too long")
    @Pattern(regexp = "^[+0-9\\s\\-()]{10,20}$",
            message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Message field is required!")
    @Size(max = 300, message = "Message cannot exceed 300 characters")
    private String message;

    @NotBlank(message = "Account name field is required!")
    @Size(min = 3, max = 50, message = "Account name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
            message = "Account name can only contain letters, numbers, dots, underscores and hyphens")
    private String nameAccount;
}