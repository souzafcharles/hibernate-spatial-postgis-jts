package com.github.souzafcharles.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JacksonConfigTest {

    private final JacksonConfig jacksonConfig = new JacksonConfig();

    // ------------------------------------------------------------
    // TEST: ObjectMapper Bean Creation
    // ------------------------------------------------------------
    @Test
    void objectMapper_WhenCreated_ShouldNotBeNull() {
        // Arrange & Act
        ObjectMapper objectMapper = jacksonConfig.objectMapper();

        // Assert
        assertNotNull(objectMapper);
    }

    // ------------------------------------------------------------
    // TEST: ObjectMapper Contains Custom Module
    // ------------------------------------------------------------
    @Test
    void objectMapper_WhenCreated_ShouldHaveCustomModule() {
        // Arrange & Act
        ObjectMapper objectMapper = jacksonConfig.objectMapper();

        // Assert
        assertNotNull(objectMapper);

        // Check if any modules are registered
        assertFalse(objectMapper.getRegisteredModuleIds().isEmpty(),
                "ObjectMapper should have at least one module registered");
    }

    // ------------------------------------------------------------
    // TEST: ObjectMapper Configuration
    // ------------------------------------------------------------
    @Test
    void objectMapper_WhenCreated_ShouldBeProperlyConfigured() {
        // Arrange & Act
        ObjectMapper objectMapper = jacksonConfig.objectMapper();

        // Assert
        assertNotNull(objectMapper);

        // Basic ObjectMapper functionality check
        assertDoesNotThrow(() -> {
            String json = objectMapper.writeValueAsString(new TestObject("test"));
            assertNotNull(json);
        }, "ObjectMapper should be able to serialize basic objects");
    }

    // ------------------------------------------------------------
    // TEST: Configuration Class Initialization
    // ------------------------------------------------------------
    @Test
    void jacksonConfig_WhenInitialized_ShouldNotBeNull() {
        // Arrange & Act & Assert
        assertNotNull(jacksonConfig);
    }

    // ------------------------------------------------------------
    // TEST: Simple Module Registration
    // ------------------------------------------------------------
    @Test
    void objectMapper_ShouldRegisterCustomModule() {
        // Arrange
        ObjectMapper objectMapper = jacksonConfig.objectMapper();

        // Act
        int moduleCount = objectMapper.getRegisteredModuleIds().size();

        // Assert
        assertTrue(moduleCount >= 1, "ObjectMapper should have custom modules registered");
    }

    // ------------------------------------------------------------
    // TEST: Bean Method Returns ObjectMapper Type
    // ------------------------------------------------------------
    @Test
    void objectMapper_ShouldReturnCorrectType() {
        // Arrange & Act
        ObjectMapper objectMapper = jacksonConfig.objectMapper();

        // Assert
        assertInstanceOf(ObjectMapper.class, objectMapper, "Should return ObjectMapper instance");
    }

    // ------------------------------------------------------------
    // TEST: Configuration Annotation Present
    // ------------------------------------------------------------
    @Test
    void jacksonConfig_ShouldHaveConfigurationAnnotation() {
        // Arrange & Act
        Class<JacksonConfig> configClass = JacksonConfig.class;
        boolean hasConfigurationAnnotation = configClass.isAnnotationPresent(org.springframework.context.annotation.Configuration.class);

        // Assert
        assertTrue(hasConfigurationAnnotation, "JacksonConfig should have @Configuration annotation");
    }

    // ------------------------------------------------------------
    // TEST: Bean Annotation Present on ObjectMapper Method
    // ------------------------------------------------------------
    @Test
    void objectMapperMethod_ShouldHaveBeanAnnotation() throws NoSuchMethodException {
        // Arrange & Act
        var method = JacksonConfig.class.getMethod("objectMapper");
        boolean hasBeanAnnotation = method.isAnnotationPresent(org.springframework.context.annotation.Bean.class);

        // Assert
        assertTrue(hasBeanAnnotation, "objectMapper() method should have @Bean annotation");
    }

    // ------------------------------------------------------------
    // TEST: ObjectMapper Can Handle Basic JSON Operations
    // ------------------------------------------------------------
    @Test
    void objectMapper_ShouldSerializeAndDeserializeBasicJson() throws Exception {
        // Arrange
        ObjectMapper objectMapper = jacksonConfig.objectMapper();
        TestObject original = new TestObject("test value");

        // Act
        String json = objectMapper.writeValueAsString(original);
        TestObject deserialized = objectMapper.readValue(json, TestObject.class);

        // Assert
        assertNotNull(json);
        assertNotNull(deserialized);
        assertEquals(original.value, deserialized.value);
    }

    // ------------------------------------------------------------
    // Helper class for testing basic serialization
    // ------------------------------------------------------------
    private static class TestObject {
        public String value;

        public TestObject() {
            // Default constructor for Jackson
        }

        public TestObject(String value) {
            this.value = value;
        }
    }
}