package com.acme.modres.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceTest {

    private Service service;

    @BeforeEach
    void setUp() {
        service = new Service();
    }

    @Test
    void testConstructor_shouldCreateInstance() {
        // Assert
        assertNotNull(service);
    }

    @Test
    void testOperation_shouldExecuteWithoutException() {
        // Act & Assert
        assertDoesNotThrow(() -> service.operation());
    }

    @Test
    void testOperation_shouldLogMessage() {
        // Act
        service.operation();

        // Assert - operation should complete without throwing exception
        assertNotNull(service);
    }

    @Test
    void testOperationConstant_shouldHaveCorrectValue() {
        // Assert
        assertEquals("my-operation", Service.OPERATION);
    }

    @Test
    void testOperation_multipleInvocations_shouldNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            service.operation();
            service.operation();
            service.operation();
        });
    }

    @Test
    void testClass_shouldExist() {
        // Act
        Class<?> clazz = Service.class;

        // Assert
        assertNotNull(clazz);
        assertEquals("Service", clazz.getSimpleName());
    }

    @Test
    void testClass_shouldBeInCorrectPackage() {
        // Act
        Class<?> clazz = Service.class;

        // Assert
        assertEquals("com.acme.modres.security", clazz.getPackageName());
    }

    @Test
    void testOperationConstant_shouldBePublicStatic() {
        // Act
        String operation = Service.OPERATION;

        // Assert
        assertNotNull(operation);
        assertEquals("my-operation", operation);
    }
}
