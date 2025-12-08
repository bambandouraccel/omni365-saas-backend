package net.accel_tech.omni365_saas_api.service;

import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.entity.SurmesureForm;
import net.accel_tech.omni365_saas_api.exception.BadRequestException;
import net.accel_tech.omni365_saas_api.repository.SurMesureFormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NdourBamba18
 **/

@Service
@RequiredArgsConstructor
public class SurMesureFormService {

    private final SurMesureFormRepository surMesureFormRepository;
    private final SurMesureEmailService surMesureEmailService;

    @Transactional
    public SurmesureForm processSurMesureForm(SurmesureForm surmesureForm) {

        // Vérifier si l'email personnel est valide
        validateEmail(surmesureForm.getEmail());

        // Sauvegarder la demande en base de données
        SurmesureForm savedForm = surMesureFormRepository.save(surmesureForm);

        try {
            // Envoyer l'email à l'administrateur
            surMesureEmailService.send(savedForm);

            // Envoyer l'email de confirmation au client
            surMesureEmailService.sendConfirmationEmail(savedForm);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi des emails: " + e.getMessage());
        }

        return savedForm;
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