package net.accel_tech.omni365_saas_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author NdourBamba18
 **/

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Validated
@Data
public class MailProperties {

    @NotBlank
    private String host;

    @NotNull
    private Integer port;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String from;

    private boolean smtpAuth = true;
    private boolean smtpStarttlsEnable = true;
    private boolean smtpStarttlsRequired = true;

}