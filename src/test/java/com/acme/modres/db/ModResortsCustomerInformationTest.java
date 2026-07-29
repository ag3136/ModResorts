package com.acme.modres.db;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ModResortsCustomerInformationTest {

    private ModResortsCustomerInformation customerInfo;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerInfo = new ModResortsCustomerInformation();
    }

    @Test
    void testGetCustomerInformation_withNullDataSource_shouldReturnEmptyList() {
        // Act
        List<String> result = customerInfo.getCustomerInformation();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCustomerInformation_withException_shouldReturnEmptyList() throws SQLException {
        // Arrange
        // Inject mock datasource using reflection
        try {
            java.lang.reflect.Field field = ModResortsCustomerInformation.class.getDeclaredField("dataSource");
            field.setAccessible(true);
            field.set(customerInfo, dataSource);
        } catch (Exception e) {
            fail("Failed to inject mock datasource");
        }

        when(dataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Act
        List<String> result = customerInfo.getCustomerInformation();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCustomerInformation_shouldReturnEmptyListWhenNoDataSource() {
        // Act
        List<String> result = customerInfo.getCustomerInformation();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetCustomerInformation_shouldHandleNullPointerException() {
        // Act & Assert
        assertDoesNotThrow(() -> customerInfo.getCustomerInformation());
    }

    @Test
    void testGetCustomerInformation_shouldReturnListType() {
        // Act
        List<String> result = customerInfo.getCustomerInformation();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof List);
    }
}
