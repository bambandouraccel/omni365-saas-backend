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



		SpringApplication.run(Omni365SaasApiApplication.class, args);
		System.out.println("Server started...");
	}

}
