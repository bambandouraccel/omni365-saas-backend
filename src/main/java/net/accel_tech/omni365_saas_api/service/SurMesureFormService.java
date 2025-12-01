package net.accel_tech.omni365_saas_api.service;

import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.entity.SurmesureForm;
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
}