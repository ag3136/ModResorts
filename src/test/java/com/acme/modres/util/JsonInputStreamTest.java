package com.acme.modres.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JsonInputStreamTest {

    @TempDir
    File tempDir;

    @Test
    void testConstructor_withValidFile_shouldCreateInstance() throws IOException {
        // Arrange
        File testFile = new File(tempDir, "test.json");
        testFile.createNewFile();

        // Act
        JsonInputStream stream = new JsonInputStream(testFile);

        // Assert
        assertNotNull(stream);
        stream.close();
    }

    @Test
    void testConstructor_withNonExistentFile_shouldThrowException() {
        // Arrange
        File nonExistentFile = new File(tempDir, "nonexistent.json");

        // Act & Assert
        assertThrows(FileNotFoundException.class, () -> {
            new JsonInputStream(nonExistentFile);
        });
    }

    @Test
    void testParseJsonAs_withNonExistentFile_shouldReturnNull() throws IOException {
        // Arrange
        File testFile = new File(tempDir, "test.json");
        testFile.createNewFile();
        JsonInputStream stream = new JsonInputStream(testFile);

        // Act
        Object result = stream.parseJsonAs(String.class);
        stream.close();

        // Assert
        assertNull(result);
    }

    @Test
    void testParseJsonAs_withValidJsonFile_shouldParseCorrectly() throws IOException {
        // Arrange
        File testFile = new File(tempDir, "test.json");
        testFile.createNewFile();
        java.io.FileWriter writer = new java.io.FileWriter(testFile);
        writer.write("{\"name\":\"test\"}");
        writer.close();

        JsonInputStream stream = new JsonInputStream(testFile);

        // Act
        Object result = stream.parseJsonAs(Object.class);
        stream.close();

        // Assert
        assertNotNull(result);
    }

    @Test
    void testParseJsonAs_withEmptyFile_shouldHandleGracefully() throws IOException {
        // Arrange
        File testFile = new File(tempDir, "empty.json");
        testFile.createNewFile();
        JsonInputStream stream = new JsonInputStream(testFile);

        // Act
        Object result = stream.parseJsonAs(String.class);
        stream.close();

        // Assert - Should handle empty file gracefully
        assertDoesNotThrow(() -> stream.parseJsonAs(String.class));
    }

    @Test
    void testClose_shouldCloseStream() throws IOException {
        // Arrange
        File testFile = new File(tempDir, "test.json");
        testFile.createNewFile();
        JsonInputStream stream = new JsonInputStream(testFile);

        // Act & Assert
        assertDoesNotThrow(() -> stream.close());
    }

    @Test
    void testParseJsonAs_withNullClass_shouldHandleGracefully() throws IOException {
        // Arrange
        File testFile = new File(tempDir, "test.json");
        testFile.createNewFile();
        JsonInputStream stream = new JsonInputStream(testFile);

        // Act & Assert
        assertDoesNotThrow(() -> {
            stream.parseJsonAs(null);
            stream.close();
        });
    }

    @Test
    void testConstructor_extendsFileInputStream_shouldBeInstanceOfFileInputStream() throws IOException {
        // Arrange
        File testFile = new File(tempDir, "test.json");
        testFile.createNewFile();

        // Act
        JsonInputStream stream = new JsonInputStream(testFile);

        // Assert
        assertTrue(stream instanceof java.io.FileInputStream);
        stream.close();
    }
}
