package com.acme.modres.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CustomPermissionTest {

    @Test
    void testConstructor_withName_shouldCreateInstance() {
        // Act
        CustomPermission permission = new CustomPermission("testPermission");

        // Assert
        assertNotNull(permission);
        assertEquals("testPermission", permission.getName());
    }

    @Test
    void testConstructor_withNameAndActions_shouldCreateInstance() {
        // Act
        CustomPermission permission = new CustomPermission("testPermission", "read,write");

        // Assert
        assertNotNull(permission);
        assertEquals("testPermission", permission.getName());
    }

    @Test
    void testConstructor_withEmptyName_shouldCreateInstance() {
        // Act
        CustomPermission permission = new CustomPermission("");

        // Assert
        assertNotNull(permission);
        assertEquals("", permission.getName());
    }

    @Test
    void testConstructor_withNullActions_shouldCreateInstance() {
        // Act
        CustomPermission permission = new CustomPermission("testPermission", null);

        // Assert
        assertNotNull(permission);
        assertEquals("testPermission", permission.getName());
    }

    @Test
    void testConstructor_withEmptyActions_shouldCreateInstance() {
        // Act
        CustomPermission permission = new CustomPermission("testPermission", "");

        // Assert
        assertNotNull(permission);
        assertEquals("testPermission", permission.getName());
    }

    @Test
    void testGetName_shouldReturnCorrectName() {
        // Arrange
        CustomPermission permission = new CustomPermission("myPermission");

        // Act
        String name = permission.getName();

        // Assert
        assertEquals("myPermission", name);
    }

    @Test
    void testConstructor_withSpecialCharacters_shouldHandleCorrectly() {
        // Act
        CustomPermission permission = new CustomPermission("permission@#$");

        // Assert
        assertNotNull(permission);
        assertEquals("permission@#$", permission.getName());
    }

    @Test
    void testConstructor_withLongName_shouldHandleCorrectly() {
        // Arrange
        String longName = "veryLongPermissionNameThatShouldBeHandledCorrectly";

        // Act
        CustomPermission permission = new CustomPermission(longName);

        // Assert
        assertEquals(longName, permission.getName());
    }

    @Test
    void testConstructor_withMultipleActions_shouldCreateInstance() {
        // Act
        CustomPermission permission = new CustomPermission("testPermission", "read,write,execute");

        // Assert
        assertNotNull(permission);
        assertEquals("testPermission", permission.getName());
    }

    @Test
    void testConstructor_extendsBasicPermission_shouldBeInstanceOfBasicPermission() {
        // Act
        CustomPermission permission = new CustomPermission("testPermission");

        // Assert
        assertTrue(permission instanceof java.security.BasicPermission);
    }
}
