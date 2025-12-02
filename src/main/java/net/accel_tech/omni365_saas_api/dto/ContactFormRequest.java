package net.accel_tech.omni365_saas_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

/**
 * @author NdourBamba18
 **/

@Data
public class ContactFormRequest {

    @NotBlank(message = "Full Name field is required!")
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank(message = "Email field is required!")
    @Size(max = 180, message = "Email is too long")
    @jakarta.validation.constraints.Email(message = "Please, enter a valid email address")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format. Example: user@example.com")
    private String email;

    @NotBlank(message = "Phone Number field is required!")
    @Size(max = 20, message = "Phone number is too long")
    @Pattern(regexp = "^[+0-9\\s\\-()]{10,20}$",
            message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Message field is required!")
    @Size(max = 300)
    private String message;

    @NotBlank(message = "Enterprise Name field is required!")
    @Size(min = 2, max = 100)
    private String enterpriseName;

    @NotBlank(message = "Domain Name field is required!")
    @Size(min = 2, max = 100)
    private String domainName;

    @Max(100)
    @Min(1)
    private Integer accountNumber;

    @NotBlank(message = "Account List field is required!")
    @Size(min = 2, max = 500)
    private String accountList;

    private Date createdAt;

}
