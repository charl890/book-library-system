package com.test.book.library.system;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Combined test class for:
 * 1. Verifying Spring Boot application context loads successfully
 * 2. Ensuring main() method correctly delegates to SpringApplication.run()
 */
@SpringBootTest(properties = {
		// Use in-memory DB to avoid real PostgreSQL dependency during test
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})

class ApplicationTest {

	/**
	 * Ensures Spring Boot context loads successfully
	 * This validates:
	 * - Component scanning
	 * - Entity scanning
	 * - Repository configuration
	 */
	@Test
	void contextLoads() {
		// Passes if no exception is thrown during startup
	}

	/**
	 * Ensures main() method calls SpringApplication.run()
	 */
	@Test
	void main_ShouldInvokeSpringApplicationRun() {

		try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {

			ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);

			mockedSpringApplication
					.when(() -> SpringApplication.run(eq(Application.class), any(String[].class)))
					.thenReturn(mockContext);

			String[] args = { "--spring.profiles.active=test" };

			// Execute main method
			Application.main(args);

			// Verify SpringApplication.run was called correctly
			mockedSpringApplication.verify(() -> SpringApplication.run(Application.class, args));
		}
	}
}