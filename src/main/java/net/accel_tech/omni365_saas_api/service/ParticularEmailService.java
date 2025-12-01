package net.accel_tech.omni365_saas_api.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.entity.ParticularForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author NdourBamba18
 **/

@Service
@RequiredArgsConstructor
public class ParticularEmailService {

    Logger logger = LogManager.getLogger(SurMesureEmailService.class);

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String emailTo;

    @Async
    public void send(ParticularForm particularForm) {

        // prepare email format
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
                mimeMessage.setSubject("Nouvelle Demande de Configuration Particulier - " + particularForm.getLastName() + " " + particularForm.getFirstName());

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                String emailContent = buildEmailTemplate(particularForm);
                helper.setText(emailContent, true);
            }
        };

        try {
            emailSender.send(preparator);
            logger.info("Email sent with success.");
        } catch (Exception e) {
            logger.error("Error sending email.", e);
            throw e;
        }
    }

    private String buildEmailTemplate(ParticularForm particularForm) {
        return "<!DOCTYPE html>" +
                "<html lang=\"fr\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Demande de Configuration</title>" +
                "    <style>" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
                "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f5f7fa; }" +
                "        .container { max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px 20px; text-align: center; }" +
                "        .header h1 { font-size: 24px; font-weight: 600; margin-bottom: 10px; }" +
                "        .header p { font-size: 14px; opacity: 0.9; }" +
                "        .content { padding: 30px; }" +
                "        .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-bottom: 25px; }" +
                "        .info-card { background: #f8f9fa; padding: 15px; border-radius: 8px; border-left: 4px solid #667eea; }" +
                "        .info-label { font-size: 12px; color: #6c757d; font-weight: 600; text-transform: uppercase; margin-bottom: 5px; }" +
                "        .info-value { font-size: 14px; color: #212529; font-weight: 500; }" +
                "        .message-section { background: #f8f9fa; padding: 20px; border-radius: 8px; margin-top: 20px; }" +
                "        .message-label { font-size: 14px; color: #6c757d; font-weight: 600; margin-bottom: 10px; }" +
                "        .message-content { font-size: 14px; color: #212529; line-height: 1.5; }" +
                "        .footer { background: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #6c757d; border-top: 1px solid #e9ecef; }" +
                "        .logo { font-size: 18px; font-weight: bold; color: #667eea; margin-bottom: 10px; }" +
                "        @media (max-width: 600px) { " +
                "            .info-grid { grid-template-columns: 1fr; }" +
                "            .content { padding: 20px; }" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h1>Demande de configuration personnalisée - Omni365 SaaS</h1>" +
                "            <p>Nouvelle demande reçue</p>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <div class=\"info-grid\">" +
                "                <div class=\"info-card\">" +
                "                    <div class=\"info-label\">Expéditeur</div>" +
                "                    <div class=\"info-value\">" + escapeHtml(particularForm.getFirstName()) + "</div>" +
                "                </div>" +
                "                <div class=\"info-card\">" +
                "                    <div class=\"info-label\">Expéditeur</div>" +
                "                    <div class=\"info-value\">" + escapeHtml(particularForm.getLastName()) + "</div>" +
                "                </div>" +
                "                <div class=\"info-card\">" +
                "                    <div class=\"info-label\">Email</div>" +
                "                    <div class=\"info-value\">" + escapeHtml(particularForm.getPersonalEmail()) + "</div>" +
                "                </div>" +
                "                <div class=\"info-card\">" +
                "                    <div class=\"info-label\">Téléphone</div>" +
                "                    <div class=\"info-value\">" + escapeHtml(particularForm.getPhoneNumber()) + "</div>" +
                "                </div>" +
                "                <div class=\"info-card\">" +
                "                    <div class=\"info-label\">Entreprise</div>" +
                "                    <div class=\"info-value\">" + escapeHtml(particularForm.getNameAccount()) + "</div>" +
                "                </div>" +
                "            </div>" +
                "            " +
                "            " +
                "            <div class=\"message-section\">" +
                "                <div class=\"message-label\">MESSAGE</div>" +
                "                <div class=\"message-content\">" + escapeHtml(particularForm.getMessage()) + "</div>" +
                "            </div>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <div class=\"logo\" style=\"text-align: center;\">Omni365 SaaS</div>" +
                "            <p style=\"text-align: center;\">Merci de traiter cette demande dans les 24 heures.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    @Async
    public void sendConfirmationEmail(ParticularForm particularForm) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(particularForm.getPersonalEmail()));
                mimeMessage.setSubject("Confirmation de votre demande particuler - Omni365 SaaS");

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                String emailContent = buildConfirmationEmailTemplate(particularForm);
                helper.setText(emailContent, true);
            }
        };

        try {
            emailSender.send(preparator);
            logger.info("Confirmation email sent successfully to: {}", particularForm.getPersonalEmail());
        } catch (Exception e) {
            logger.error("Error sending confirmation email to: {}", particularForm.getPersonalEmail(), e);
            throw e;
        }
    }

    private String buildConfirmationEmailTemplate(ParticularForm particularForm) {
        return "<!DOCTYPE html>" +
                "<html lang=\"fr\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Confirmation de Demande Sur Mesure</title>" +
                "    <style>" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
                "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f5f7fa; }" +
                "        .container { max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px 20px; text-align: center; }" +
                "        .header h1 { font-size: 24px; font-weight: 600; margin-bottom: 10px; }" +
                "        .content { padding: 30px; }" +
                "        .info-card { background: #f0f4ff; padding: 20px; border-radius: 8px; border-left: 4px solid #667eea; margin-bottom: 20px; }" +
                "        .info-label { font-size: 12px; color: #5d6fd8; font-weight: 600; text-transform: uppercase; margin-bottom: 5px; }" +
                "        .info-value { font-size: 14px; color: #212529; font-weight: 500; }" +
                "        .message-section { background: #f8f9fa; padding: 20px; border-radius: 8px; margin-top: 20px; }" +
                "        .steps { margin: 25px 0; }" +
                "        .step { display: flex; align-items: center; margin-bottom: 15px; }" +
                "        .step-number { " +
                "            background: #667eea; " +
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
                "        .step-text { flex: 1; }" +
                "        .footer { background: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #6c757d; border-top: 1px solid #e9ecef; }" +
                "        .logo { font-size: 18px; font-weight: bold; color: #667eea; margin-bottom: 10px; }" +
                "        .thank-you { text-align: center; margin-bottom: 25px; }" +
                "        .highlight { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px; border-radius: 8px; text-align: center; margin: 20px 0; }" +
                "        @media (max-width: 600px) { " +
                "            .content { padding: 20px; }" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h1>Confirmation de Réception</h1>" +
                "            <p>Votre demande sur mesure a bien été enregistrée</p>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <div class=\"thank-you\">" +
                "                <h2>Merci, " + escapeHtml(particularForm.getLastName())+ " " + escapeHtml(particularForm.getFirstName()) + " !</h2>" +
                "                <p>Nous avons bien reçu votre demande de configuration <strong>particulier</strong> pour <strong>" + escapeHtml(particularForm.getNameAccount()) + "</strong>.</p>" +
                "            </div>" +
                "            " +
                "            <div class=\"highlight\">" +
                "                <h3>✨ Solution Personnalisée</h3>" +
                "                <p>Votre demande fera l'objet d'une analyse approfondie pour créer une solution adaptée à vos besoins spécifiques.</p>" +
                "            </div>" +
                "            " +
                "            <div class=\"info-card\">" +
                "                <div class=\"info-label\">RÉSUMÉ DE VOTRE DEMANDE SUR MESURE</div>" +
                "                <div class=\"info-value\"><strong>Nom de Compte:</strong> " + escapeHtml(particularForm.getNameAccount()) + "</div>" +
                "                <div class=\"info-value\"><strong>Numero de Telephone:</strong> " + particularForm.getPhoneNumber() + "</div>" +
                "                <div class=\"info-value\"><strong>Référence:</strong> OMNI-SM-" + System.currentTimeMillis() + "</div>" +
                "            </div>" +
                "            " +
                "            <div class=\"steps\">" +
                "                <h3>Prochaines étapes :</h3>" +
                "                <div class=\"step\">" +
                "                    <div class=\"step-number\">1</div>" +
                "                    <div class=\"step-text\"><strong>Analyse approfondie</strong> de vos besoins spécifiques sous 48h</div>" +
                "                </div>" +
                "                <div class=\"step\">" +
                "                    <div class=\"step-number\">2</div>" +
                "                    <div class=\"step-text\"><strong>Étude de faisabilité</strong> et proposition technique personnalisée</div>" +
                "                </div>" +
                "                <div class=\"step\">" +
                "                    <div class=\"step-number\">3</div>" +
                "                    <div class=\"step-text\"><strong>Contact dédié</strong> par notre équipe d'experts</div>" +
                "                </div>" +
                "                <div class=\"step\">" +
                "                    <div class=\"step-number\">4</div>" +
                "                    <div class=\"step-text\"><strong>Développement sur mesure</strong> et déploiement de votre solution</div>" +
                "                </div>" +
                "            </div>" +
                "            " +
                "            <div class=\"message-section\">" +
                "                <p><strong>Votre message :</strong></p>" +
                "                <p>\"" + escapeHtml(particularForm.getMessage()) + "\"</p>" +
                "            </div>" +
                "            " +
                "            <div style=\"text-align: center; margin-top: 25px;\">" +
                "                <p><strong>Notre engagement :</strong></p>" +
                "                <p>Nous mettons tout en œuvre pour comprendre vos besoins uniques et vous proposer la solution la plus adaptée.</p>" +
                "                <p>Contactez-nous à <a href=\"mailto:omni365-support@heritage.africa\">omni365-support@heritage.africa</a></p>" +
                "            </div>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <div class=\"logo\">Omni365 SaaS - Solutions Sur Mesure</div>" +
                "            <p>Votre partenaire pour des solutions cloud personnalisées</p>" +
                "            <p>&copy; 2025 Omni365 SaaS. Tous droits réservés.</p>" +
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
