package hb.swgohbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main Class and Application's entry point.
 *
 * @author Hector Blanco
 */
@SpringBootApplication(scanBasePackages = "hb.swgohbot")
public class Application {
	
	/**
	 * Application's entry point. Starts SpringBoot Application.
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
