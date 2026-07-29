package com.acme.modres;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConstantsTest {

    @Test
    void testBarcelonaConstant_shouldHaveCorrectValue() {
        // Assert
        assertEquals("Barcelona", Constants.BARCELONA);
    }

    @Test
    void testCorkConstant_shouldHaveCorrectValue() {
        // Assert
        assertEquals("Cork", Constants.CORK);
    }

    @Test
    void testMiamiConstant_shouldHaveCorrectValue() {
        // Assert
        assertEquals("Miami", Constants.MIAMI);
    }

    @Test
    void testSanFranciscoConstant_shouldHaveCorrectValue() {
        // Assert
        assertEquals("San_Francisco", Constants.SAN_FRANCISCO);
    }

    @Test
    void testParisConstant_shouldHaveCorrectValue() {
        // Assert
        assertEquals("Paris", Constants.PARIS);
    }

    @Test
    void testLasVegasConstant_shouldHaveCorrectValue() {
        // Assert
        assertEquals("Las_Vegas", Constants.LAS_VEGAS);
    }

    @Test
    void testSupportedCitiesArray_shouldContainAllCities() {
        // Assert
        assertNotNull(Constants.SUPPORTED_CITIES);
        assertEquals(6, Constants.SUPPORTED_CITIES.length);
        assertTrue(arrayContains(Constants.SUPPORTED_CITIES, Constants.PARIS));
        assertTrue(arrayContains(Constants.SUPPORTED_CITIES, Constants.LAS_VEGAS));
        assertTrue(arrayContains(Constants.SUPPORTED_CITIES, Constants.SAN_FRANCISCO));
        assertTrue(arrayContains(Constants.SUPPORTED_CITIES, Constants.MIAMI));
        assertTrue(arrayContains(Constants.SUPPORTED_CITIES, Constants.CORK));
        assertTrue(arrayContains(Constants.SUPPORTED_CITIES, Constants.BARCELONA));
    }

    @Test
    void testBarcelonaWeatherFile_shouldHaveCorrectValue() {
        // Assert
        assertEquals("barcelona.json", Constants.BACELONA_WEATHER_FILE);
    }

    @Test
    void testCorkWeatherFile_shouldHaveCorrectValue() {
        // Assert
        assertEquals("cork.json", Constants.CORK_WEATHER_FILE);
    }

    @Test
    void testLasVegasWeatherFile_shouldHaveCorrectValue() {
        // Assert
        assertEquals("nv.json", Constants.LAS_VEGAS_WEATHER_FILE);
    }

    @Test
    void testMiamiWeatherFile_shouldHaveCorrectValue() {
        // Assert
        assertEquals("miami.json", Constants.MIAMI_WEATHER_FILE);
    }

    @Test
    void testParisWeatherFile_shouldHaveCorrectValue() {
        // Assert
        assertEquals("paris.json", Constants.PARIS_WEATHER_FILE);
    }

    @Test
    void testSanFranciscoWeatherFile_shouldHaveCorrectValue() {
        // Assert
        assertEquals("sanfran.json", Constants.SAN_FRANCESCO_WEATHER_FILE);
    }

    @Test
    void testWundergroundApiPrefix_shouldHaveCorrectValue() {
        // Assert
        assertEquals("http://api.wunderground.com/api/", Constants.WUNDERGROUND_API_PREFIX);
    }

    @Test
    void testWundergroundApiPart_shouldHaveCorrectValue() {
        // Assert
        assertEquals("/forecast/geolookup/conditions/q/", Constants.WUNDERGROUND_API_PART);
    }

    @Test
    void testDataFormat_shouldHaveCorrectValue() {
        // Assert
        assertEquals("MM/dd/yyyy", Constants.DATA_FORMAT);
    }

    private boolean arrayContains(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
