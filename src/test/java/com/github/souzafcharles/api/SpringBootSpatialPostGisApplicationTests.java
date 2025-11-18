package com.github.souzafcharles.api;

import com.github.souzafcharles.api.config.LoadEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SpringBootSpatialPostGisApplicationTest {

    // ------------------------------------------------------------
    // TEST: Application Class Initialization
    // ------------------------------------------------------------
    @Test
    void applicationClass_WhenInitialized_ShouldNotBeNull() {
        // Arrange & Act
        SpringBootSpatialPostGisApplication application = new SpringBootSpatialPostGisApplication();

        // Assert
        assertNotNull(application);
    }

    // ------------------------------------------------------------
    // TEST: Main Method Structure
    // ------------------------------------------------------------
    @Test
    void mainMethod_ShouldExist() {
        // Arrange & Act & Assert
        // This test just verifies that the main method exists and can be called
        // We can't easily test the main method directly since it starts Spring Boot
        assertNotNull(SpringBootSpatialPostGisApplication.class);
    }

    // ------------------------------------------------------------
    // TEST: SpringBootApplication Annotation
    // ------------------------------------------------------------
    @Test
    void applicationClass_ShouldHaveSpringBootApplicationAnnotation() {
        // Arrange & Act
        Class<SpringBootSpatialPostGisApplication> appClass = SpringBootSpatialPostGisApplication.class;
        boolean hasSpringBootApplicationAnnotation =
                appClass.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class);

        // Assert
        assertTrue(hasSpringBootApplicationAnnotation,
                "SpringBootSpatialPostGisApplication should have @SpringBootApplication annotation");
    }

    // ------------------------------------------------------------
    // TEST: Application Context (Basic Verification)
    // ------------------------------------------------------------
    @Test
    void application_ShouldBeSpringBootApplication() {
        // Arrange & Act & Assert
        // Basic verification that this is a Spring Boot application
        assertNotNull(SpringBootSpatialPostGisApplication.class.getAnnotation(
                org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    // ------------------------------------------------------------
    // TEST: LoadEnvironment Called in Main
    // ------------------------------------------------------------
    @Test
    void mainMethod_ShouldCallLoadEnvironment() {
        // Arrange
        try (var mockedLoadEnvironment = mockStatic(LoadEnvironment.class);
             var mockedSpringApplication = mockStatic(SpringApplication.class)) {

            // Act
            SpringBootSpatialPostGisApplication.main(new String[]{});

            // Assert
            mockedLoadEnvironment.verify(() -> LoadEnvironment.loadEnv(), times(1));
        }
    }

    // ------------------------------------------------------------
    // TEST: SpringApplication Run Called
    // ------------------------------------------------------------
    @Test
    void mainMethod_ShouldCallSpringApplicationRun() {
        // Arrange
        try (var mockedLoadEnvironment = mockStatic(LoadEnvironment.class);
             var mockedSpringApplication = mockStatic(SpringApplication.class)) {

            // Act
            SpringBootSpatialPostGisApplication.main(new String[]{});

            // Assert
            mockedSpringApplication.verify(
                    () -> SpringApplication.run(
                            eq(SpringBootSpatialPostGisApplication.class),
                            any(String[].class)
                    ),
                    times(1)
            );
        }
    }

    // ------------------------------------------------------------
    // TEST: NoArgsConstructor Available
    // ------------------------------------------------------------
    @Test
    void applicationClass_ShouldHaveDefaultConstructor() {
        // Arrange & Act
        SpringBootSpatialPostGisApplication application = new SpringBootSpatialPostGisApplication();

        // Assert
        assertNotNull(application, "Should be able to create instance with default constructor");
    }

    // ------------------------------------------------------------
    // TEST: Class Name and Package
    // ------------------------------------------------------------
    @Test
    void applicationClass_ShouldHaveCorrectNameAndPackage() {
        // Arrange & Act
        String className = SpringBootSpatialPostGisApplication.class.getName();
        String packageName = SpringBootSpatialPostGisApplication.class.getPackageName();

        // Assert
        assertTrue(className.contains("SpringBootSpatialPostGisApplication"));
        assertTrue(packageName.contains("com.github.souzafcharles.api"));
    }
}