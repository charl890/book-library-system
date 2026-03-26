package com.test.book.library.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for the Test Library Management System.
 * This class serves as the entry point for the Spring Boot application,
 * bootstrapping the application context and starting the embedded server.
 * It is annotated with @SpringBootApplication, which
 * encompasses @Configuration,
 * @EnableAutoConfiguration, and @ComponentScan annotations, enabling
 * auto-configuration
 * and component scanning in the specified packages.
 */
@SpringBootApplication
@EntityScan("com.test.book.library.system.entity")
@EnableJpaRepositories("com.test.book.library.system.repository")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
