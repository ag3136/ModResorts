package com.acme.modres.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ZipValidatorTest {

    @TempDir
    File tempDir;

    @Test
    void testConstructor_withValidZipFile_shouldCreateInstance() throws IOException {
        // Arrange
        File zipFile = createValidZipFile("test.zip");

        // Act
        ZipValidator validator = new ZipValidator(zipFile);

        // Assert
        assertNotNull(validator);
        validator.close();
    }

    @Test
    void testConstructor_withNonZipFile_shouldThrowException() throws IOException {
        // Arrange
        File nonZipFile = new File(tempDir, "notazip.txt");
        nonZipFile.createNewFile();

        // Act & Assert
        assertThrows(ZipException.class, () -> {
            new ZipValidator(nonZipFile);
        });
    }

    @Test
    void testConstructor_withNonExistentFile_shouldThrowException() {
        // Arrange
        File nonExistentFile = new File(tempDir, "nonexistent.zip");

        // Act & Assert
        assertThrows(IOException.class, () -> {
            new ZipValidator(nonExistentFile);
        });
    }

    @Test
    void testIsValid_withEmptyZipFile_shouldReturnTrue() throws Throwable {
        // Arrange
        File zipFile = createEmptyZipFile("empty.zip");
        ZipValidator validator = new ZipValidator(zipFile);

        // Act
        boolean result = validator.isValid();
        validator.close();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsValid_withValidZipFile_shouldReturnFalse() throws Throwable {
        // Arrange
        File zipFile = createValidZipFile("valid.zip");
        ZipValidator validator = new ZipValidator(zipFile);

        // Act
        boolean result = validator.isValid();
        validator.close();

        // Assert
        assertFalse(result); // Returns false when zip has entries
    }

    @Test
    void testClose_shouldCloseZipFile() throws IOException {
        // Arrange
        File zipFile = createValidZipFile("test.zip");
        ZipValidator validator = new ZipValidator(zipFile);

        // Act & Assert
        assertDoesNotThrow(() -> validator.close());
    }

    @Test
    void testClose_multipleTimes_shouldNotThrowException() throws IOException {
        // Arrange
        File zipFile = createValidZipFile("test.zip");
        ZipValidator validator = new ZipValidator(zipFile);

        // Act & Assert
        assertDoesNotThrow(() -> {
            validator.close();
            validator.close();
        });
    }

    @Test
    void testImplementsAutoCloseable_shouldBeAutoCloseable() throws IOException {
        // Arrange
        File zipFile = createValidZipFile("test.zip");

        // Act & Assert
        try (ZipValidator validator = new ZipValidator(zipFile)) {
            assertNotNull(validator);
        }
    }

    @Test
    void testIsValid_afterClose_shouldHandleGracefully() throws IOException {
        // Arrange
        File zipFile = createEmptyZipFile("test.zip");
        ZipValidator validator = new ZipValidator(zipFile);
        validator.close();

        // Act & Assert
        assertThrows(Throwable.class, () -> validator.isValid());
    }

    private File createValidZipFile(String filename) throws IOException {
        File zipFile = new File(tempDir, filename);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            ZipEntry entry = new ZipEntry("test.txt");
            zos.putNextEntry(entry);
            zos.write("test content".getBytes());
            zos.closeEntry();
        }
        return zipFile;
    }

    private File createEmptyZipFile(String filename) throws IOException {
        File zipFile = new File(tempDir, filename);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            // Create empty zip file
        }
        return zipFile;
    }
}
