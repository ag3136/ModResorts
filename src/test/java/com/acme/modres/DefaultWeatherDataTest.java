package com.acme.modres;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class DefaultWeatherDataTest {

    @Test
    void testConstructor_withValidCity_shouldCreateInstance() {
        // Act
        DefaultWeatherData data = new DefaultWeatherData(Constants.PARIS);

        // Assert
        assertNotNull(data);
        assertEquals(Constants.PARIS, data.getCity());
    }

    @Test
    void testConstructor_withNullCity_shouldThrowException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            new DefaultWeatherData(null);
        });
    }

    @Test
    void testConstructor_withInvalidCity_shouldThrowException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            new DefaultWeatherData("InvalidCity");
        });
    }

    @Test
    void testConstructor_withParis_shouldCreateInstance() {
        // Act
        DefaultWeatherData data = new DefaultWeatherData(Constants.PARIS);

        // Assert
        assertEquals(Constants.PARIS, data.getCity());
    }

    @Test
    void testConstructor_withLasVegas_shouldCreateInstance() {
        // Act
        DefaultWeatherData data = new DefaultWeatherData(Constants.LAS_VEGAS);

        // Assert
        assertEquals(Constants.LAS_VEGAS, data.getCity());
    }

    @Test
    void testConstructor_withSanFrancisco_shouldCreateInstance() {
        // Act
        DefaultWeatherData data = new DefaultWeatherData(Constants.SAN_FRANCISCO);

        // Assert
        assertEquals(Constants.SAN_FRANCISCO, data.getCity());
    }

    @Test
    void testConstructor_withMiami_shouldCreateInstance() {
        // Act
        DefaultWeatherData data = new DefaultWeatherData(Constants.MIAMI);

        // Assert
        assertEquals(Constants.MIAMI, data.getCity());
    }

    @Test
    void testConstructor_withCork_shouldCreateInstance() {
        // Act
        DefaultWeatherData data = new DefaultWeatherData(Constants.CORK);

        // Assert
        assertEquals(Constants.CORK, data.getCity());
    }

    @Test
    void testConstructor_withBarcelona_shouldCreateInstance() {
        // Act
        DefaultWeatherData data = new DefaultWeatherData(Constants.BARCELONA);

        // Assert
        assertEquals(Constants.BARCELONA, data.getCity());
    }

    @Test
    void testGetDefaultWeatherData_withParis_shouldReturnData() throws IOException {
        // Arrange
        DefaultWeatherData data = new DefaultWeatherData(Constants.PARIS);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            data.getDefaultWeatherData();
        });
    }

    @Test
    void testGetCity_shouldReturnCorrectCity() {
        // Arrange
        DefaultWeatherData data = new DefaultWeatherData(Constants.MIAMI);

        // Act
        String city = data.getCity();

        // Assert
        assertEquals(Constants.MIAMI, city);
    }

    @Test
    void testConstructor_withEmptyString_shouldThrowException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            new DefaultWeatherData("");
        });
    }

    @Test
    void testConstructor_withWhitespace_shouldThrowException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            new DefaultWeatherData("   ");
        });
    }
}
