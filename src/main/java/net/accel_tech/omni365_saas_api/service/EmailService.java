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
public class EmailService {

	Logger logger = LogManager.getLogger(EmailService.class);

	private final JavaMailSender emailSender;
	private final ContactFormRepository contactFormRepository;

	@Value("${spring.mail.username}")
	private String emailTo;

	public ContactForm save(ContactForm contactForm) {
		return contactFormRepository.save(contactForm);
	}

	@Async
	public void send(ContactForm contactForm) {

		// prepare email format
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
				mimeMessage.setSubject("Nouvelle Demande de Configuration - " + contactForm.getEnterpriseName());

				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

				String emailContent = buildEmailTemplate(contactForm);
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
				"            <h1>Demande de Configuration Omni365 SaaS</h1>" +
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