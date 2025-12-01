package net.accel_tech.omni365_saas_api.service;

import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.entity.ParticularForm;
import net.accel_tech.omni365_saas_api.exception.BadRequestException;
import net.accel_tech.omni365_saas_api.repository.ParticularFormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NdourBamba18
 **/

@Service
@RequiredArgsConstructor
public class ParticularFormService {

    private final ParticularFormRepository particularFormRepository;
    private final ParticularEmailService particularEmailService;

    @Transactional
    public ParticularForm processParticularForm(ParticularForm particularForm) {

        // Vérifier si l'email personnel est valide
        validateEmail(particularForm.getPersonalEmail());

        // Vérifier si le nom de compte est disponible
        validateAccountName(particularForm.getNameAccount());

        // Vérifier si l'email personnel existe déjà
        if (particularFormRepository.existsByPersonalEmail(particularForm.getPersonalEmail())) {
            throw new BadRequestException("This personal email is already registered");
        }

        // Vérifier si le nom de compte existe déjà
        if (particularFormRepository.existsByNameAccount(particularForm.getNameAccount())) {
            throw new BadRequestException("This account name is already taken. Please choose another one");
        }

        // Sauvegarder la demande
        ParticularForm savedForm = particularFormRepository.save(particularForm);

        try {
            // Envoyer l'email à l'administrateur
            particularEmailService.send(savedForm);

            // Envoyer l'email de confirmation au client
            particularEmailService.sendConfirmationEmail(savedForm);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi des emails: " + e.getMessage());
            // On peut choisir de ne pas rollback la transaction si les emails échouent
        }

        return savedForm;
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Personal email is required");
        }

        // Validation simple du format
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new BadRequestException("Invalid email format. Example: user@example.com");
        }

        // Vérifier les domaines invalides
        String lowerEmail = email.toLowerCase();
        if (lowerEmail.endsWith("@gafa.com")) {
            throw new BadRequestException("Personal email cannot be a Gafa email. Please use your personal email address");
        }
    }

    private void validateAccountName(String accountName) {
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new BadRequestException("Account name is required");
        }

        String cleanName = accountName.trim().toLowerCase();

        if (cleanName.length() < 3 || cleanName.length() > 50) {
            throw new BadRequestException("Account name must be between 3 and 50 characters");
        }

        if (!cleanName.matches("^[a-z0-9._-]+$")) {
            throw new BadRequestException("Account name can only contain lowercase letters, numbers, dots, underscores and hyphens");
        }

        // Empêcher les noms réservés
        String[] reservedNames = {"admin", "root", "system", "support", "info", "contact",
                "mail", "postmaster", "hostmaster", "webmaster"};
        for (String reserved : reservedNames) {
            if (cleanName.equals(reserved) || cleanName.startsWith(reserved + ".")) {
                throw new BadRequestException("This account name is reserved. Please choose another one");
            }
        }

        // Empêcher les caractères spéciaux problématiques
        if (cleanName.contains("..") || cleanName.endsWith(".") || cleanName.startsWith(".")) {
            throw new BadRequestException("Account name cannot start or end with a dot, or contain consecutive dots");
        }
    }
}