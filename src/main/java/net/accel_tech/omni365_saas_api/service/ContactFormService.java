package net.accel_tech.omni365_saas_api.service;

import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.entity.ContactForm;
import net.accel_tech.omni365_saas_api.exception.BadRequestException;
import net.accel_tech.omni365_saas_api.repository.ContactFormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NdourBamba18
 **/

@Service
@RequiredArgsConstructor
public class ContactFormService {

    private final ContactFormRepository contactFormRepository;
    private final ContactEmailService emailService;

    @Transactional
    public ContactForm processContactForm(ContactForm contactForm) {

        // Vérifier si l'email personnel est valide
        validateEmail(contactForm.getEmail());

        // Sauvegarder la demande en base de données
        ContactForm savedContactForm = contactFormRepository.save(contactForm);

        try {
            // Envoyer l'email à l'administrateur
            emailService.send(savedContactForm);

            // Envoyer l'email de confirmation au client
            emailService.sendConfirmationEmail(savedContactForm);

        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer la sauvegarde
            System.err.println("Erreur lors de l'envoi des emails: " + e.getMessage());
        }

        return savedContactForm;
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email is required");
        }

        // Validation simple du format
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new BadRequestException("Invalid email format. Example: user@example.com");
        }

        // Vérifier les domaines invalides
        String lowerEmail = email.toLowerCase();
        if (lowerEmail.endsWith("@heritage.africa")) {
            throw new BadRequestException("Personal email cannot be a Heritage email. " +
                    "Please use your personal email address");
        }

        // Vérifier les domaines invalides
        String lowerEmailAccel = email.toLowerCase();
        if (lowerEmailAccel.endsWith("@accel-tech.net")) {
            throw new BadRequestException("Personal email cannot be a Accel Tech email. " +
                    "Please use your personal email address");
        }
    }
}