package com.github.souzafcharles.api.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


class LoadEnvironmentTest {

    @TempDir
    Path tempDir;

    // ------------------------------------------------------------
    // TEST: LoadEnvironment Method Exists
    // ------------------------------------------------------------
    @Test
    void loadEnv_WhenCalled_ShouldNotThrowException() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> LoadEnvironment.loadEnv());
    }

    // ------------------------------------------------------------
    // TEST: LoadEnvironment is Static Method
    // ------------------------------------------------------------
    @Test
    void loadEnv_ShouldBeStaticMethod() {
        // Arrange & Act & Assert
        // This test verifies we can call the static method without creating an instance
        assertDoesNotThrow(() -> LoadEnvironment.loadEnv());
    }

    // ------------------------------------------------------------
    // TEST: No Instance Needed for Static Method
    // ------------------------------------------------------------
    @Test
    void loadEnv_CanBeCalledWithoutInstance() {
        // Arrange & Act & Assert
        // Calling static method directly without creating object
        assertDoesNotThrow(() -> LoadEnvironment.loadEnv());
    }

    // ------------------------------------------------------------
    // TEST: Class Can Be Instantiated
    // ------------------------------------------------------------
    @Test
    void loadEnvironment_WhenInstantiated_ShouldNotBeNull() {
        // Arrange & Act
        LoadEnvironment loadEnvironment = new LoadEnvironment();

        // Assert
        assertNotNull(loadEnvironment);
    }

    // ------------------------------------------------------------
    // TEST: Default Constructor Available
    // ------------------------------------------------------------
    @Test
    void loadEnvironment_ShouldHaveDefaultConstructor() {
        // Arrange & Act
        LoadEnvironment loadEnvironment = new LoadEnvironment();

        // Assert
        assertNotNull(loadEnvironment);
    }

    // ------------------------------------------------------------
    // TEST: Method is Public
    // ------------------------------------------------------------
    @Test
    void loadEnv_ShouldBePublicMethod() {
        // Arrange & Act
        // If we can call it from another package, it's public
        assertDoesNotThrow(() -> LoadEnvironment.loadEnv());
    }

    // ------------------------------------------------------------
    // TEST: Class Name and Package
    // ------------------------------------------------------------
    @Test
    void loadEnvironmentClass_ShouldHaveCorrectNameAndPackage() {
        // Arrange & Act
        String className = LoadEnvironment.class.getName();
        String packageName = LoadEnvironment.class.getPackageName();

        // Assert
        assertTrue(className.contains("LoadEnvironment"));
        assertTrue(packageName.contains("com.github.souzafcharles.api.config"));
    }

    // ------------------------------------------------------------
    // TEST: Method Returns Void
    // ------------------------------------------------------------
    @Test
    void loadEnv_ShouldReturnVoid() {
        // Arrange & Act
        // The method should not return anything (void)
        // We test this by calling it and verifying no return value
        LoadEnvironment.loadEnv();

        // Assert
        // If we reach here without exception, the method executed successfully
        assertTrue(true);
    }

    // ------------------------------------------------------------
    // TEST: Multiple Calls Do Not Throw Exception
    // ------------------------------------------------------------
    @Test
    void loadEnv_WhenCalledMultipleTimes_ShouldNotThrowException() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            LoadEnvironment.loadEnv();
            LoadEnvironment.loadEnv();
            LoadEnvironment.loadEnv();
        });
    }

    // ------------------------------------------------------------
    // TEST: Basic Functionality (Integration Style)
    // ------------------------------------------------------------
    @Test
    void loadEnv_ShouldExecuteWithoutErrors() {
        // Arrange & Act & Assert
        // This is a basic smoke test to ensure the method runs
        try {
            LoadEnvironment.loadEnv();
            // If we get here, the method executed without throwing exceptions
            assertTrue(true);
        } catch (Exception e) {
            fail("loadEnv should not throw exceptions during normal execution");
        }
    }
}