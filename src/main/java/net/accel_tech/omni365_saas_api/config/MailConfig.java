package net.accel_tech.omni365_saas_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author NdourBamba18
 **/

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configuration directe - pas de variables
        mailSender.setHost("smtp.heritage.africa");
        mailSender.setPort(587);
        mailSender.setUsername("support@omail.africa");
        mailSender.setPassword("Horizon@2027");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        // FORCER l'adresse FROM
        props.put("mail.smtp.from", "support@omail.africa");

        // Timeouts
        props.put("mail.smtp.connectiontimeout", "15000");
        props.put("mail.smtp.timeout", "15000");
        props.put("mail.smtp.writetimeout", "15000");

        // Debug
        props.put("mail.debug", "true");

        return mailSender;
    }
}