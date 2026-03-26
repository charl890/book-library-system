package com.test.book.library.system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAPI documentation for the library
 * system API.
 * This class defines the OpenAPI bean with custom information about the API,
 * such as
 * title, description, and contact details.
 */
@Configuration
public class OpenApiConfig {

    // Define the OpenAPI bean to customize the API documentation
    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI().info(new Info().title("Test Library Management System")
                .description("REST API for managing books, borrowers, and borrow/return operations.")
                .contact(new Contact().name("test").email("support@test.example")));
    }
}
