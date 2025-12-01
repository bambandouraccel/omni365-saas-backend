package net.accel_tech.omni365_saas_api.service;

import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.entity.ContactForm;
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
    private final EmailService emailService;

    @Transactional
    public ContactForm processContactForm(ContactForm contactForm) {
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
}