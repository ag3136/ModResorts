package com.acme.modres.mbean;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpMetadataTest {

    private OpMetadata opMetadata;

    @BeforeEach
    void setUp() {
        opMetadata = new OpMetadata();
    }

    @Test
    void testDefaultConstructor_shouldCreateInstance() {
        // Assert
        assertNotNull(opMetadata);
    }

    @Test
    void testParameterizedConstructor_shouldSetAllFields() {
        // Act
        OpMetadata metadata = new OpMetadata("testOp", "Test operation", "String", 1);

        // Assert
        assertEquals("testOp", metadata.getName());
        assertEquals("Test operation", metadata.getDescription());
        assertEquals("String", metadata.getType());
        assertEquals(1, metadata.getImpact());
    }

    @Test
    void testSetName_shouldSetName() {
        // Act
        opMetadata.setName("operationName");

        // Assert
        assertEquals("operationName", opMetadata.getName());
    }

    @Test
    void testSetDescription_shouldSetDescription() {
        // Act
        opMetadata.setDescription("Operation description");

        // Assert
        assertEquals("Operation description", opMetadata.getDescription());
    }

    @Test
    void testSetType_shouldSetType() {
        // Act
        opMetadata.setType("void");

        // Assert
        assertEquals("void", opMetadata.getType());
    }

    @Test
    void testSetImpact_shouldSetImpact() {
        // Act
        opMetadata.setImpact(2);

        // Assert
        assertEquals(2, opMetadata.getImpact());
    }

    @Test
    void testGetName_withNullName_shouldReturnNull() {
        // Act
        String result = opMetadata.getName();

        // Assert
        assertNull(result);
    }

    @Test
    void testGetDescription_withNullDescription_shouldReturnNull() {
        // Act
        String result = opMetadata.getDescription();

        // Assert
        assertNull(result);
    }

    @Test
    void testGetType_withNullType_shouldReturnNull() {
        // Act
        String result = opMetadata.getType();

        // Assert
        assertNull(result);
    }

    @Test
    void testGetImpact_withDefaultValue_shouldReturnZero() {
        // Act
        int result = opMetadata.getImpact();

        // Assert
        assertEquals(0, result);
    }

    @Test
    void testSetName_withEmptyString_shouldSetEmptyString() {
        // Act
        opMetadata.setName("");

        // Assert
        assertEquals("", opMetadata.getName());
    }

    @Test
    void testSetName_withNull_shouldSetNull() {
        // Act
        opMetadata.setName(null);

        // Assert
        assertNull(opMetadata.getName());
    }

    @Test
    void testSetImpact_withNegativeValue_shouldSetNegativeValue() {
        // Act
        opMetadata.setImpact(-1);

        // Assert
        assertEquals(-1, opMetadata.getImpact());
    }

    @Test
    void testParameterizedConstructor_withNullValues_shouldHandleGracefully() {
        // Act
        OpMetadata metadata = new OpMetadata(null, null, null, 0);

        // Assert
        assertNull(metadata.getName());
        assertNull(metadata.getDescription());
        assertNull(metadata.getType());
        assertEquals(0, metadata.getImpact());
    }
}
