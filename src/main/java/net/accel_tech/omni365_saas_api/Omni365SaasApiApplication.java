package net.accel_tech.omni365_saas_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author NdourBamba18
 **/

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableAutoConfiguration
public class Omni365SaasApiApplication {


	public static void main(String[] args) {

		//Dotenv dotenv = Dotenv.load();
		// Database Service Configuration
		/*System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		System.setProperty("SPRING_DATASOURCE_PLATFORM", dotenv.get("SPRING_DATASOURCE_PLATFORM"));*/

		// Email Service Configuration
		/*System.setProperty("SPRING_MAIL_HOST", dotenv.get("SPRING_MAIL_HOST"));
		System.setProperty("SPRING_MAIL_PORT", dotenv.get("SPRING_MAIL_PORT"));
		System.setProperty("SPRING_MAIL_USERNAME", dotenv.get("SPRING_MAIL_USERNAME"));
		System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));
		System.setProperty("SPRING_MAIL_FROM", dotenv.get("SPRING_MAIL_FROM"));*/


		// Advanced SMTP Service Configuration
		/*System.setProperty("SPRING_MAIL_SMTP_AUTH", dotenv.get("SPRING_MAIL_SMTP_AUTH"));
		System.setProperty("SPRING_MAIL_SMTP_STARTTLS_ENABLE", dotenv.get("SPRING_MAIL_SMTP_STARTTLS_ENABLE"));
		System.setProperty("SPRING_MAIL_SMTP_STARTTLS_REQUIRED", dotenv.get("SPRING_MAIL_SMTP_STARTTLS_REQUIRED"));*/

		// Application Port Configuration
		//System.setProperty("PORT", dotenv.get("PORT"));

		SpringApplication.run(Omni365SaasApiApplication.class, args);
		System.out.println("Server started...");
	}

}
