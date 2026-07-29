package com.acme.modres.mbean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppInfoTest {

    private AppInfo appInfo;

    @BeforeEach
    void setUp() {
        appInfo = new AppInfo();
    }

    @Test
    void testConstructor_shouldInitializeMBeanInfo() {
        // Assert
        assertNotNull(appInfo);
        assertNotNull(appInfo.getMBeanInfo());
    }

    @Test
    void testGetMBeanInfo_shouldReturnMBeanInfo() {
        // Act
        MBeanInfo info = appInfo.getMBeanInfo();

        // Assert
        assertNotNull(info);
        assertEquals(AppInfo.class.getName(), info.getClassName());
    }

    @Test
    void testInvoke_withIncreaseMaxLimit_shouldReturnMessage() throws MBeanException, ReflectionException {
        // Act
        Object result = appInfo.invoke("increaseMaxLimit", new Object[0], new String[0]);

        // Assert
        assertNotNull(result);
        assertEquals("Max limit increased", result);
    }

    @Test
    void testInvoke_withResetMaxLimit_shouldReturnMessage() throws MBeanException, ReflectionException {
        // Act
        Object result = appInfo.invoke("resetMaxLimit", new Object[0], new String[0]);

        // Assert
        assertNotNull(result);
        assertEquals("Max limit reset", result);
    }

    @Test
    void testInvoke_withUnsupportedOperation_shouldThrowException() {
        // Act & Assert
        assertThrows(MBeanException.class, () -> {
            appInfo.invoke("unsupportedOperation", new Object[0], new String[0]);
        });
    }

    @Test
    void testInvoke_withNullActionName_shouldThrowException() {
        // Act & Assert
        assertThrows(MBeanException.class, () -> {
            appInfo.invoke(null, new Object[0], new String[0]);
        });
    }

    @Test
    void testInvoke_withEmptyActionName_shouldThrowException() {
        // Act & Assert
        assertThrows(MBeanException.class, () -> {
            appInfo.invoke("", new Object[0], new String[0]);
        });
    }

    @Test
    void testGetAttribute_shouldReturnNull() throws AttributeNotFoundException, MBeanException, ReflectionException {
        // Act
        Object result = appInfo.getAttribute("anyAttribute");

        // Assert
        assertNull(result);
    }

    @Test
    void testSetAttribute_shouldNotThrowException() {
        // Arrange
        Attribute attribute = new Attribute("test", "value");

        // Act & Assert
        assertDoesNotThrow(() -> appInfo.setAttribute(attribute));
    }

    @Test
    void testGetAttributes_shouldReturnNull() {
        // Act
        AttributeList result = appInfo.getAttributes(new String[]{"attr1", "attr2"});

        // Assert
        assertNull(result);
    }

    @Test
    void testSetAttributes_shouldReturnNull() {
        // Arrange
        AttributeList attributes = new AttributeList();

        // Act
        AttributeList result = appInfo.setAttributes(attributes);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetMBeanInfo_shouldHaveCorrectDescription() {
        // Act
        MBeanInfo info = appInfo.getMBeanInfo();

        // Assert
        assertEquals("Configurable App Info", info.getDescription());
    }

    @Test
    void testInvoke_withIncreaseMaxLimit_shouldExecuteWithoutError() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            appInfo.invoke("increaseMaxLimit", new Object[0], new String[0]);
        });
    }

    @Test
    void testInvoke_withResetMaxLimit_shouldExecuteWithoutError() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            appInfo.invoke("resetMaxLimit", new Object[0], new String[0]);
        });
    }
}
