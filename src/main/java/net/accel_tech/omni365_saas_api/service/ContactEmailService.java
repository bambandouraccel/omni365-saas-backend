package net.accel_tech.omni365_saas_api.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.entity.ContactForm;
import net.accel_tech.omni365_saas_api.repository.ContactFormRepository;
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
public class ContactEmailService {

	Logger logger = LogManager.getLogger(ContactEmailService.class);

	private final JavaMailSender emailSender;
	private final ContactFormRepository contactFormRepository;

	@Value("${spring.mail.username}")
	private String emailFrom; // RENOMMÉ : c'est l'expéditeur, pas le destinataire

	//@Value("${admin.email:support@omail.africa}")
	@Value("${admin.email}")
	private String adminEmail;

	public ContactForm save(ContactForm contactForm) {
		return contactFormRepository.save(contactForm);
	}

	@Async
	public void send(ContactForm contactForm) {

		// prepare email format
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				// DÉFINIR L'EXPÉDITEUR CORRECTEMENT
				mimeMessage.setFrom(new InternetAddress(emailFrom));

				// DESTINATAIRE : l'admin, pas l'expéditeur
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(adminEmail));
				mimeMessage.setSubject("Nouvelle Demande de Configuration - " + contactForm.getEnterpriseName());

				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

				String emailContent = buildEmailTemplate(contactForm);
				helper.setText(emailContent, true);
			}
		};
		try {
			emailSender.send(preparator);
			logger.info("Email sent with success to admin: {}", adminEmail);
		} catch (Exception e) {
			logger.error("Error sending email.", e);
			throw e;
		}
	}

	private String buildEmailTemplate(ContactForm contactForm) {
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
				"        .header { background: linear-gradient(135deg, #ff8c00 0%, #ff6b00 100%); color: white; padding: 30px 20px; text-align: center; }" +
				"        .header h1 { font-size: 24px; font-weight: 600; margin-bottom: 10px; }" +
				"        .header p { font-size: 14px; opacity: 0.9; }" +
				"        .content { padding: 30px; }" +
				"        .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-bottom: 25px; }" +
				"        .info-card { background: #fff5e6; padding: 15px; border-radius: 8px; border-left: 4px solid #ff8c00; }" +
				"        .info-label { font-size: 12px; color: #cc5500; font-weight: 600; text-transform: uppercase; margin-bottom: 5px; }" +
				"        .info-value { font-size: 14px; color: #212529; font-weight: 500; }" +
				"        .message-section { background: #fff5e6; padding: 20px; border-radius: 8px; margin-top: 20px; border-left: 4px solid #ff8c00; }" +
				"        .message-label { font-size: 14px; color: #cc5500; font-weight: 600; margin-bottom: 10px; }" +
				"        .message-content { font-size: 14px; color: #212529; line-height: 1.5; }" +
				"        .footer { background: #fff5e6; padding: 20px; text-align: center; font-size: 12px; color: #cc5500; border-top: 1px solid #ffd699; }" +
				"        .logo { font-size: 18px; font-weight: bold; color: #ff8c00; margin-bottom: 10px; }" +
				"        @media (max-width: 600px) { " +
				"            .info-grid { grid-template-columns: 1fr; }" +
				"            .content { padding: 20px; }" +
				"        }" +
				"    </style>" +
				"</head>" +
				"<body>" +
				"    <div class=\"container\">" +
				"        <div class=\"header\">" +
				"            <h1>Demande de Configuration Omni365 pour TPE/PME</h1>" +
				"            <p>Nouvelle demande reçue</p>" +
				"        </div>" +
				"        <div class=\"content\">" +
				"            <div class=\"info-grid\">" +
				"                <div class=\"info-card\">" +
				"                    <div class=\"info-label\">Expéditeur</div>" +
				"                    <div class=\"info-value\">" + escapeHtml(contactForm.getFullName()) + "</div>" +
				"                </div>" +
				"                <div class=\"info-card\">" +
				"                    <div class=\"info-label\">Email</div>" +
				"                    <div class=\"info-value\">" + escapeHtml(contactForm.getEmail()) + "</div>" +
				"                </div>" +
				"                <div class=\"info-card\">" +
				"                    <div class=\"info-label\">Téléphone</div>" +
				"                    <div class=\"info-value\">" + escapeHtml(contactForm.getPhoneNumber()) + "</div>" +
				"                </div>" +
				"                <div class=\"info-card\">" +
				"                    <div class=\"info-label\">Entreprise</div>" +
				"                    <div class=\"info-value\">" + escapeHtml(contactForm.getEnterpriseName()) + "</div>" +
				"                </div>" +
				"                <div class=\"info-card\">" +
				"                    <div class=\"info-label\">Domaine</div>" +
				"                    <div class=\"info-value\">" + escapeHtml(contactForm.getDomainName()) + "</div>" +
				"                </div>" +
				"                <div class=\"info-card\">" +
				"                    <div class=\"info-label\">Nombre de comptes</div>" +
				"                    <div class=\"info-value\">" + contactForm.getAccountNumber() + "</div>" +
				"                </div>" +
				"            </div>" +
				"            " +
				"            <div class=\"info-card\">" +
				"                <div class=\"info-label\">Liste des comptes</div>" +
				"                <div class=\"info-value\">" + escapeHtml(contactForm.getAccountList()) + "</div>" +
				"            </div>" +
				"            " +
				"            <div class=\"message-section\">" +
				"                <div class=\"message-label\">MESSAGE</div>" +
				"                <div class=\"message-content\">" + escapeHtml(contactForm.getMessage()) + "</div>" +
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
	public void sendConfirmationEmail(ContactForm contactForm) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				// DÉFINIR L'EXPÉDITEUR CORRECTEMENT
				mimeMessage.setFrom(new InternetAddress(emailFrom));

				// DESTINATAIRE : l'utilisateur qui a soumis le formulaire
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(contactForm.getEmail()));
				mimeMessage.setSubject("Confirmation de votre demande TPE/PME - Omni365");

				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

				String emailContent = buildConfirmationEmailTemplate(contactForm);
				helper.setText(emailContent, true);
			}
		};

		try {
			emailSender.send(preparator);
			logger.info("Confirmation email sent successfully to: {}", contactForm.getEmail());
		} catch (Exception e) {
			logger.error("Error sending confirmation email to: {}", contactForm.getEmail(), e);
			throw e;
		}
	}

	private String buildConfirmationEmailTemplate(ContactForm contactForm) {
		return "<!DOCTYPE html>" +
				"<html lang=\"fr\">" +
				"<head>" +
				"    <meta charset=\"UTF-8\">" +
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
				"    <title>Confirmation de Demande</title>" +
				"    <style>" +
				"        * { margin: 0; padding: 0; box-sizing: border-box; }" +
				"        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background-color: #f5f7fa; }" +
				"        .container { max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 20px; overflow: hidden; box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1); }" +
				"        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%); color: white; padding: 40px 20px; text-align: center; position: relative; }" +
				"        .header h1 { font-size: 28px; font-weight: 700; margin-bottom: 10px; }" +
				"        .header p { font-size: 16px; opacity: 0.9; }" +
				"        .content { padding: 40px; }" +
				"        .info-card { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white; padding: 25px; border-radius: 15px; margin-bottom: 25px; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1); }" +
				"        .info-label { font-size: 12px; color: rgba(255,255,255,0.8); font-weight: 600; text-transform: uppercase; margin-bottom: 8px; letter-spacing: 1px; }" +
				"        .info-value { font-size: 15px; color: white; font-weight: 500; }" +
				"        .message-section { background: #f8f9fa; padding: 25px; border-radius: 15px; margin-top: 25px; border-left: 5px solid #667eea; }" +
				"        " +
				"        /* STYLES FUN & MODERNES */" +
				"        .next-steps { margin: 35px 0; text-align: center; }" +
				"        .countdown-card { " +
				"            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); " +
				"            color: white; " +
				"            padding: 30px; " +
				"            border-radius: 20px; " +
				"            box-shadow: 0 10px 30px rgba(79, 172, 254, 0.3); " +
				"            position: relative; " +
				"            overflow: hidden; " +
				"            margin: 25px 0; " +
				"        }" +
				"        .countdown-card::before {" +
				"            content: '';" +
				"            position: absolute;" +
				"            top: -50%;" +
				"            left: -50%;" +
				"            width: 200%;" +
				"            height: 200%;" +
				"            background: linear-gradient(45deg, transparent, rgba(255,255,255,0.1), transparent);" +
				"            transform: rotate(45deg);" +
				"            animation: shine 3s infinite;" +
				"        }" +
				"        @keyframes shine {" +
				"            0% { transform: translateX(-100%) rotate(45deg); }" +
				"            100% { transform: translateX(100%) rotate(45deg); }" +
				"        }" +
				"        .rocket-icon { " +
				"            font-size: 48px; " +
				"            margin-bottom: 15px; " +
				"            display: block; " +
				"            animation: float 3s ease-in-out infinite; " +
				"        }" +
				"        @keyframes float {" +
				"            0%, 100% { transform: translateY(0px); }" +
				"            50% { transform: translateY(-10px); }" +
				"        }" +
				"        .countdown-text {" +
				"            font-size: 22px; " +
				"            font-weight: 700; " +
				"            margin: 15px 0; " +
				"            text-shadow: 0 2px 4px rgba(0,0,0,0.2);" +
				"        }" +
				"        .timer { " +
				"            font-size: 32px; " +
				"            font-weight: 800; " +
				"            background: rgba(255,255,255,0.2); " +
				"            padding: 10px 20px; " +
				"            border-radius: 50px; " +
				"            display: inline-block; " +
				"            margin: 10px 0; " +
				"            backdrop-filter: blur(10px);" +
				"        }" +
				"        .emoji-steps {" +
				"            display: flex;" +
				"            justify-content: center;" +
				"            gap: 20px;" +
				"            margin-top: 25px;" +
				"            flex-wrap: wrap;" +
				"        }" +
				"        .emoji-step {" +
				"            text-align: center;" +
				"            background: white;" +
				"            padding: 20px 15px;" +
				"            border-radius: 15px;" +
				"            box-shadow: 0 5px 15px rgba(0,0,0,0.1);" +
				"            flex: 1;" +
				"            min-width: 120px;" +
				"        }" +
				"        .emoji {" +
				"            font-size: 32px;" +
				"            margin-bottom: 10px;" +
				"            display: block;" +
				"        }" +
				"        .step-desc {" +
				"            font-size: 12px;" +
				"            color: #666;" +
				"            font-weight: 600;" +
				"        }" +
				"        " +
				"        .footer { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px 20px; text-align: center; }" +
				"        .logo { font-size: 24px; font-weight: bold; margin-bottom: 10px; }" +
				"        .thank-you { text-align: center; margin-bottom: 30px; }" +
				"        .thank-you h2 { " +
				"            background: linear-gradient(135deg, #667eea, #f093fb); " +
				"            -webkit-background-clip: text; " +
				"            -webkit-text-fill-color: transparent; " +
				"            font-size: 32px; " +
				"            font-weight: 800; " +
				"            margin-bottom: 15px; " +
				"        }" +
				"        @media (max-width: 600px) { " +
				"            .content { padding: 25px; }" +
				"            .emoji-steps { flex-direction: column; }" +
				"            .countdown-text { font-size: 18px; }" +
				"            .timer { font-size: 24px; }" +
				"        }" +
				"    </style>" +
				"</head>" +
				"<body>" +
				"    <div class=\"container\">" +
				"        <div class=\"header\">" +
				"            <h1>Votre demande de TPE/PME a été bien enregistrée</h1>" +
				"            <p>Votre aventure Omni365 commence maintenant</p>" +
				"        </div>" +
				"        <div class=\"content\">" +
				"            <div class=\"thank-you\">" +
				"                <h2>Bienvenue à bord, " + escapeHtml(contactForm.getFullName()) + " !</h2>" +
				"                <p style=\"font-size: 18px; color: #666;\">Votre environnement <strong>" + escapeHtml(contactForm.getEnterpriseName()) + "</strong> est en cours de préparation 🚀</p>" +
				"            </div>" +
				"            " +
				"            <div class=\"info-card\">" +
				"                <div class=\"info-label\">VOTRE ESPACE PERSONNALISÉ</div>" +
				"                <div class=\"info-value\"><strong>🏢 Entreprise:</strong> " + escapeHtml(contactForm.getEnterpriseName()) + "</div>" +
				"                <div class=\"info-value\"><strong>🌐 Domaine:</strong> " + escapeHtml(contactForm.getDomainName()) + "</div>" +
				"                <div class=\"info-value\"><strong>👥 Comptes:</strong> " + contactForm.getAccountNumber() + " utilisateurs</div>" +
				"                <div class=\"info-value\"><strong>📋 Référence:</strong> OMNI-" + System.currentTimeMillis() + "</div>" +
				"            </div>" +
				"            " +
				"            <div class=\"next-steps\">" +
				"                <h3 style=\"text-align: center; color: #333; margin-bottom: 30px; font-size: 24px;\">🎯 C'est Parti !</h3>" +
				"                " +
				"                <div class=\"countdown-card\">" +
				"                    <span class=\"rocket-icon\">Omni365</span>" +
				"                    <div class=\"countdown-text\">Vos identifiants arrivent !</div>" +
				"                    <div class=\"timer\">⏳ 24H MAX</div>" +
				"                    <div style=\"font-size: 14px; opacity: 0.9;\">Préparez-vous à décoller !</div>" +
				"                </div>" +
				"                " +
				"                <div class=\"emoji-steps\">" +
				"                    <div class=\"emoji-step\">" +
				"                        <span class=\"emoji\">📨</span>" +
				"                        <div class=\"step-desc\">Email avec vos identifiants</div>" +
				"                    </div>" +
				"                    <div class=\"emoji-step\">" +
				"                        <span class=\"emoji\">🔐</span>" +
				"                        <div class=\"step-desc\">Accès à votre espace</div>" +
				"                    </div>" +
				"                    <div class=\"emoji-step\">" +
				"                        <span class=\"emoji\">🎊</span>" +
				"                        <div class=\"step-desc\">Démarrage immédiat</div>" +
				"                    </div>" +
				"                </div>" +
				"            </div>" +
				"            " +
				"            <div style=\"text-align: center; margin-top: 35px; padding: 25px; background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); border-radius: 15px;\">" +
				"                <p style=\"font-weight: 600; color: #333; margin-bottom: 10px;\">❔ Une question ?</p>" +
				"                <p style=\"font-size: 18px;\">" +
				"                    <a href=\"mailto:support@omail.africa\" style=\"color: #667eea; text-decoration: none; font-weight: 700;\">" +
				"                        📧 support@omail.africa" +
				"                    </a>" +
				"                </p>" +
				"            </div>" +
				"        </div>" +
				"        <div class=\"footer\">" +
				"            <div class=\"logo\">Omni365</div>" +
				"            <p>Votre succès, notre mission ✨</p>" +
				"            <p style=\"opacity: 0.8; margin-top: 10px;\">&copy; 2025 Omni365 SaaS. Prêt pour l'innovation !</p>" +
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