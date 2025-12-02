package net.accel_tech.omni365_saas_api.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.entity.ParticularForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticularEmailService {

    Logger logger = LogManager.getLogger(ParticularEmailService.class);

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailFrom; // RENOMMÉ : c'est l'expéditeur

    @Value("${admin.email:support@omail.africa}")
    private String adminEmail; // Destinataire admin

    @Async
    public void send(ParticularForm particularForm) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // DÉFINIR L'EXPÉDITEUR (CRITIQUE)
            helper.setFrom(emailFrom);

            // DESTINATAIRE : l'admin
            helper.setTo(adminEmail);
            helper.setSubject("Nouvelle Demande Particulier Omni365 - " +
                    particularForm.getLastName() + " " + particularForm.getFirstName());

            String emailContent = buildEmailTemplate(particularForm);
            helper.setText(emailContent, true);

            emailSender.send(mimeMessage);
            logger.info("Email particulier envoyé avec succès à l'admin : {}", adminEmail);

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email particulier à l'admin.", e);
            throw new RuntimeException("Erreur d'envoi d'email particulier", e);
        }
    }

    @Async
    public void sendConfirmationEmail(ParticularForm particularForm) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // DÉFINIR L'EXPÉDITEUR (CRITIQUE)
            helper.setFrom(emailFrom);

            // DESTINATAIRE : l'utilisateur particulier
            helper.setTo(particularForm.getPersonalEmail());
            helper.setSubject("Confirmation de votre compte particulier - Omni365");

            String emailContent = buildConfirmationEmailTemplate(particularForm);
            helper.setText(emailContent, true);

            emailSender.send(mimeMessage);
            logger.info("Email de confirmation particulier envoyé avec succès à : {}",
                    particularForm.getPersonalEmail());

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email de confirmation particulier à : {}",
                    particularForm.getPersonalEmail(), e);
            throw new RuntimeException("Erreur d'envoi d'email de confirmation particulier", e);
        }
    }

    private String buildEmailTemplate(ParticularForm particularForm) {
        return "<!DOCTYPE html>" +
                "<html lang=\"fr\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Demande Particulier</title>" +
                "    <style>" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
                "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f5f7fa; }" +
                "        .container { max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }" +
                "        .header { background: linear-gradient(135deg, #00b09b 0%, #96c93d 100%); color: white; padding: 30px 20px; text-align: center; }" +
                "        .header h1 { font-size: 24px; font-weight: 600; margin-bottom: 10px; }" +
                "        .header p { font-size: 14px; opacity: 0.9; }" +
                "        .content { padding: 30px; }" +
                "        .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-bottom: 25px; }" +
                "        .info-card { background: #f0fff4; padding: 15px; border-radius: 8px; border-left: 4px solid #00b09b; }" +
                "        .info-label { font-size: 12px; color: #00b09b; font-weight: 600; text-transform: uppercase; margin-bottom: 5px; }" +
                "        .info-value { font-size: 14px; color: #212529; font-weight: 500; }" +
                "        .message-section { background: #f0fff4; padding: 20px; border-radius: 8px; margin-top: 20px; }" +
                "        .message-label { font-size: 14px; color: #00b09b; font-weight: 600; margin-bottom: 10px; }" +
                "        .message-content { font-size: 14px; color: #212529; line-height: 1.5; }" +
                "        .footer { background: #f0fff4; padding: 20px; text-align: center; font-size: 12px; color: #00b09b; border-top: 1px solid #c6f7e2; }" +
                "        .logo { font-size: 18px; font-weight: bold; color: #00b09b; margin-bottom: 10px; }" +
                "        .gafa-account { " +
                "            background: #00b09b; " +
                "            color: white; " +
                "            padding: 10px 15px; " +
                "            border-radius: 20px; " +
                "            display: inline-block; " +
                "            font-weight: bold; " +
                "            margin-top: 5px; " +
                "        }" +
                "        @media (max-width: 600px) { " +
                "            .info-grid { grid-template-columns: 1fr; }" +
                "            .content { padding: 20px; }" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h1>Nouvelle Demande Particulier - Omni365</h1>" +
                "            <p>Demande de compte particulier</p>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <div class=\"info-grid\">" +
                "                <div class=\"info-card\">" +
                "                    <div class=\"info-label\">Prénom</div>" +
                "                    <div class=\"info-value\">" + escapeHtml(particularForm.getFirstName()) + "</div>" +
                "                </div>" +
                "                <div class=\"info-card\">" +
                "                    <div class=\"info-label\">Nom</div>" +
                "                    <div class=\"info-value\">" + escapeHtml(particularForm.getLastName()) + "</div>" +
                "                </div>" +
                "                <div class=\"info-card\">" +
                "                    <div class=\"info-label\">Email Personnel</div>" +
                "                    <div class=\"info-value\">" + escapeHtml(particularForm.getPersonalEmail()) + "</div>" +
                "                </div>" +
                "                <div class=\"info-card\">" +
                "                    <div class=\"info-label\">Téléphone</div>" +
                "                    <div class=\"info-value\">" + escapeHtml(particularForm.getPhoneNumber()) + "</div>" +
                "                </div>" +
                "            </div>" +
                "            " +
                "            <div class=\"info-card\">" +
                "                <div class=\"info-label\">COMPTE OMNI365 CRÉÉ</div>" +
                "                <div class=\"info-value\">" +
                "                    <div class=\"gafa-account\">" + particularForm.getFullAccountEmail() + "</div>" +
                "                    <div style=\"font-size: 12px; color: #666; margin-top: 5px;\">" +
                "                        (Basé sur: " + escapeHtml(particularForm.getNameAccount()) + ")" +
                "                    </div>" +
                "                </div>" +
                "            </div>" +
                "            " +
                "            <div class=\"message-section\">" +
                "                <div class=\"message-label\">MESSAGE DU CLIENT</div>" +
                "                <div class=\"message-content\">" + escapeHtml(particularForm.getMessage()) + "</div>" +
                "            </div>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <div class=\"logo\">Omni365 SaaS</div>" +
                "            <p>Service pour particuliers</p>" +
                "            <p style=\"margin-top: 10px;\">Merci de traiter cette demande dans les 24 heures.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    private String buildConfirmationEmailTemplate(ParticularForm particularForm) {
        String omniEmail = particularForm.getFullAccountEmail();

        return "<!DOCTYPE html>" +
                "<html lang=\"fr\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Confirmation de Compte Particulier Omni365</title>" +
                "    <style>" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
                "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f5f7fa; }" +
                "        .container { max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 20px; overflow: hidden; box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1); }" +
                "        .header { background: linear-gradient(135deg, #00b09b 0%, #96c93d 100%); color: white; padding: 40px 20px; text-align: center; }" +
                "        .header h1 { font-size: 28px; font-weight: 700; margin-bottom: 10px; }" +
                "        .content { padding: 40px; }" +
                "        .account-card { " +
                "            background: linear-gradient(135deg, #00b09b 0%, #96c93d 100%); " +
                "            color: white; " +
                "            padding: 30px; " +
                "            border-radius: 15px; " +
                "            text-align: center; " +
                "            margin: 25px 0; " +
                "            box-shadow: 0 10px 30px rgba(0, 176, 155, 0.3);" +
                "        }" +
                "        .account-email { " +
                "            font-size: 24px; " +
                "            font-weight: 800; " +
                "            background: rgba(255,255,255,0.2); " +
                "            padding: 15px 25px; " +
                "            border-radius: 50px; " +
                "            display: inline-block; " +
                "            margin: 15px 0; " +
                "            letter-spacing: 1px;" +
                "        }" +
                "        .emoji-big { font-size: 48px; margin-bottom: 20px; }" +
                "        .steps { margin: 30px 0; }" +
                "        .step { display: flex; align-items: center; margin-bottom: 15px; }" +
                "        .step-number { " +
                "            background: #00b09b; " +
                "            color: white; " +
                "            width: 30px; " +
                "            height: 30px; " +
                "            border-radius: 50%; " +
                "            display: flex; " +
                "            align-items: center; " +
                "            justify-content: center; " +
                "            margin-right: 15px; " +
                "            font-weight: bold; " +
                "            flex-shrink: 0; " +
                "        }" +
                "        .footer { background: #f8f9fa; padding: 30px; text-align: center; color: #666; border-top: 1px solid #dee2e6; }" +
                "        .info-summary { " +
                "            background: #f0fff4; " +
                "            padding: 20px; " +
                "            border-radius: 15px; " +
                "            margin-bottom: 25px; " +
                "        }" +
                "        @media (max-width: 600px) { " +
                "            .content { padding: 25px; }" +
                "            .account-email { font-size: 20px; padding: 12px 20px; }" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h1>🎉 Votre Compte Particulier Omni365 est Prêt !</h1>" +
                "            <p>Bienvenue dans la communauté Omni365</p>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <div style=\"text-align: center; margin-bottom: 30px;\">" +
                "                <h2 style=\"color: #00b09b;\">Bonjour " + escapeHtml(particularForm.getFirstName()) + " " + escapeHtml(particularForm.getLastName()) + " !</h2>" +
                "                <p>Votre compte particuliers a été créé avec succès.</p>" +
                "            </div>" +
                "            " +
                "            <div class=\"info-summary\">" +
                "                <h3 style=\"color: #00b09b; margin-bottom: 15px;\">📋 Récapitulatif</h3>" +
                "                <p><strong>Email personnel :</strong> " + escapeHtml(particularForm.getPersonalEmail()) + "</p>" +
                "                <p><strong>Téléphone :</strong> " + escapeHtml(particularForm.getPhoneNumber()) + "</p>" +
                "                <p><strong>Référence :</strong> OMNI-" + System.currentTimeMillis() + "</p>" +
                "            </div>" +
                "            " +
                "            <div class=\"account-card\">" +
                "                <div class=\"emoji-big\">📧</div>" +
                "                <div style=\"font-size: 18px; margin-bottom: 10px;\">Votre adresse Omni365 :</div>" +
                "                <div class=\"account-email\">" + omniEmail + "</div>" +
                "                <div style=\"font-size: 14px; opacity: 0.9; margin-top: 10px;\">" +
                "                    (Identifiants à venir dans 24h)" +
                "                </div>" +
                "            </div>" +
                "            " +
                "            <div class=\"steps\">" +
                "                <h3 style=\"color: #333; margin-bottom: 20px;\">🎯 Prochaines étapes :</h3>" +
                "                <div class=\"step\">" +
                "                    <div class=\"step-number\">1</div>" +
                "                    <div><strong>Réception de vos identifiants</strong> sous 24h</div>" +
                "                </div>" +
                "                <div class=\"step\">" +
                "                    <div class=\"step-number\">2</div>" +
                "                    <div><strong>Connexion à votre espace</strong> Omni365</div>" +
                "                </div>" +
                "                <div class=\"step\">" +
                "                    <div class=\"step-number\">3</div>" +
                "                    <div><strong>Découverte des fonctionnalités</strong> Omni365</div>" +
                "                </div>" +
                "            </div>" +
                "            " +
                "            <div style=\"text-align: center; margin-top: 40px; padding: 25px; background: #f0fff4; border-radius: 15px;\">" +
                "                <p style=\"font-weight: 600; color: #00b09b; margin-bottom: 10px;\">❔ Une question ?</p>" +
                "                <p style=\"font-size: 18px;\">" +
                "                    <a href=\"mailto:support@omail.africa\" " +
                "                       style=\"color: #00b09b; text-decoration: none; font-weight: 700;\">" +
                "                        📧 support@omail.africa" +
                "                    </a>" +
                "                </p>" +
                "            </div>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <div style=\"font-size: 20px; font-weight: bold; color: #00b09b; margin-bottom: 10px;\">Omni365</div>" +
                "            <p>Votre solution cloud personnelle ✨</p>" +
                "            <p style=\"margin-top: 10px; font-size: 14px; color: #666;\">" +
                "                &copy; 2025 Omni365 SaaS. Tous droits réservés." +
                "            </p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}