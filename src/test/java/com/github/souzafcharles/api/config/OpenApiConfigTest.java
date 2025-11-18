package com.github.souzafcharles.api.config;

import com.github.souzafcharles.api.utils.Messages;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    private final OpenApiConfig openApiConfig = new OpenApiConfig();

    // ------------------------------------------------------------
    // TEST: OpenAPI Bean Creation
    // ------------------------------------------------------------
    @Test
    void customOpenAPI_WhenCreated_ShouldNotBeNull() {
        // Arrange & Act
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        // Assert
        assertNotNull(openAPI);
    }

    // ------------------------------------------------------------
    // TEST: OpenAPI Contains Info
    // ------------------------------------------------------------
    @Test
    void customOpenAPI_WhenCreated_ShouldHaveInfo() {
        // Arrange & Act
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        // Assert
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
    }

    // ------------------------------------------------------------
    // TEST: OpenAPI Info Has Correct Title
    // ------------------------------------------------------------
    @Test
    void customOpenAPI_ShouldHaveCorrectTitle() {
        // Arrange & Act
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        // Assert
        assertNotNull(info);
        assertEquals(Messages.OPENAPI_TITLE, info.getTitle());
    }

    // ------------------------------------------------------------
    // TEST: OpenAPI Info Has Correct Version
    // ------------------------------------------------------------
    @Test
    void customOpenAPI_ShouldHaveCorrectVersion() {
        // Arrange & Act
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        // Assert
        assertNotNull(info);
        assertEquals("1.0", info.getVersion());
    }

    // ------------------------------------------------------------
    // TEST: OpenAPI Info Has Correct Description
    // ------------------------------------------------------------
    @Test
    void customOpenAPI_ShouldHaveCorrectDescription() {
        // Arrange & Act
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        // Assert
        assertNotNull(info);
        assertEquals(Messages.OPENAPI_DESCRIPTION, info.getDescription());
    }

    // ------------------------------------------------------------
    // TEST: Configuration Class Initialization
    // ------------------------------------------------------------
    @Test
    void openApiConfig_WhenInitialized_ShouldNotBeNull() {
        // Arrange & Act & Assert
        assertNotNull(openApiConfig);
    }

    // ------------------------------------------------------------
    // TEST: Configuration Annotation Present
    // ------------------------------------------------------------
    @Test
    void openApiConfig_ShouldHaveConfigurationAnnotation() {
        // Arrange & Act
        Class<OpenApiConfig> configClass = OpenApiConfig.class;
        boolean hasConfigurationAnnotation = configClass.isAnnotationPresent(Configuration.class);

        // Assert
        assertTrue(hasConfigurationAnnotation, "OpenApiConfig should have @Configuration annotation");
    }

    // ------------------------------------------------------------
    // TEST: Bean Annotation Present on Method
    // ------------------------------------------------------------
    @Test
    void customOpenAPIMethod_ShouldHaveBeanAnnotation() throws NoSuchMethodException {
        // Arrange & Act
        var method = OpenApiConfig.class.getMethod("customOpenAPI");
        boolean hasBeanAnnotation = method.isAnnotationPresent(Bean.class);

        // Assert
        assertTrue(hasBeanAnnotation, "customOpenAPI() method should have @Bean annotation");
    }

    // ------------------------------------------------------------
    // TEST: OpenAPI Bean Returns Correct Type
    // ------------------------------------------------------------
    @Test
    void customOpenAPI_ShouldReturnOpenAPIType() {
        // Arrange & Act
        OpenAPI openAPI = openApiConfig.customOpenAPI();

        // Assert
        assertInstanceOf(OpenAPI.class, openAPI, "Should return OpenAPI instance");
    }

    // ------------------------------------------------------------
    // TEST: Info Object Has All Required Fields
    // ------------------------------------------------------------
    @Test
    void customOpenAPI_InfoShouldHaveAllFields() {
        // Arrange & Act
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        Info info = openAPI.getInfo();

        // Assert
        assertNotNull(info);
        assertNotNull(info.getTitle());
        assertNotNull(info.getVersion());
        assertNotNull(info.getDescription());
        assertFalse(info.getTitle().isEmpty());
        assertFalse(info.getVersion().isEmpty());
        assertFalse(info.getDescription().isEmpty());
    }

    // ------------------------------------------------------------
    // TEST: Multiple Calls Return Same Configuration
    // ------------------------------------------------------------
    @Test
    void customOpenAPI_WhenCalledMultipleTimes_ShouldReturnConsistentConfiguration() {
        // Arrange & Act
        OpenAPI firstCall = openApiConfig.customOpenAPI();
        OpenAPI secondCall = openApiConfig.customOpenAPI();

        // Assert
        assertNotNull(firstCall);
        assertNotNull(secondCall);
        assertEquals(firstCall.getInfo().getTitle(), secondCall.getInfo().getTitle());
        assertEquals(firstCall.getInfo().getVersion(), secondCall.getInfo().getVersion());
        assertEquals(firstCall.getInfo().getDescription(), secondCall.getInfo().getDescription());
    }

    // ------------------------------------------------------------
    // TEST: Default Constructor Available
    // ------------------------------------------------------------
    @Test
    void openApiConfig_ShouldHaveDefaultConstructor() {
        // Arrange & Act
        OpenApiConfig config = new OpenApiConfig();

        // Assert
        assertNotNull(config);
    }
}