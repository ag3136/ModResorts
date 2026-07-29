package com.acme.modres.mbean.reservation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
    }

    @Test
    void testDefaultConstructor_shouldCreateInstance() {
        // Assert
        assertNotNull(reservation);
    }

    @Test
    void testParameterizedConstructor_shouldSetFields() {
        // Act
        Reservation res = new Reservation("08/01/2024", "08/31/2024");

        // Assert
        assertEquals("08/01/2024", res.getFromDate());
        assertEquals("08/31/2024", res.getToDate());
    }

    @Test
    void testSetFromDate_shouldSetFromDate() {
        // Act
        reservation.setFromDate("08/15/2024");

        // Assert
        assertEquals("08/15/2024", reservation.getFromDate());
    }

    @Test
    void testSetToDate_shouldSetToDate() {
        // Act
        reservation.setToDate("08/31/2024");

        // Assert
        assertEquals("08/31/2024", reservation.getToDate());
    }

    @Test
    void testGetFromDate_withNullValue_shouldReturnNull() {
        // Act
        String result = reservation.getFromDate();

        // Assert
        assertNull(result);
    }

    @Test
    void testGetToDate_withNullValue_shouldReturnNull() {
        // Act
        String result = reservation.getToDate();

        // Assert
        assertNull(result);
    }

    @Test
    void testSetFromDate_withNull_shouldSetNull() {
        // Act
        reservation.setFromDate(null);

        // Assert
        assertNull(reservation.getFromDate());
    }

    @Test
    void testSetToDate_withNull_shouldSetNull() {
        // Act
        reservation.setToDate(null);

        // Assert
        assertNull(reservation.getToDate());
    }

    @Test
    void testSetFromDate_withEmptyString_shouldSetEmptyString() {
        // Act
        reservation.setFromDate("");

        // Assert
        assertEquals("", reservation.getFromDate());
    }

    @Test
    void testSetToDate_withEmptyString_shouldSetEmptyString() {
        // Act
        reservation.setToDate("");

        // Assert
        assertEquals("", reservation.getToDate());
    }

    @Test
    void testParameterizedConstructor_withNullValues_shouldHandleGracefully() {
        // Act
        Reservation res = new Reservation(null, null);

        // Assert
        assertNull(res.getFromDate());
        assertNull(res.getToDate());
    }

    @Test
    void testSetFromDate_withInvalidFormat_shouldStillSet() {
        // Act
        reservation.setFromDate("invalid-date");

        // Assert
        assertEquals("invalid-date", reservation.getFromDate());
    }

    @Test
    void testSetToDate_withInvalidFormat_shouldStillSet() {
        // Act
        reservation.setToDate("invalid-date");

        // Assert
        assertEquals("invalid-date", reservation.getToDate());
    }

    @Test
    void testParameterizedConstructor_withDifferentFormats_shouldSetValues() {
        // Act
        Reservation res = new Reservation("2024-08-01", "2024-08-31");

        // Assert
        assertEquals("2024-08-01", res.getFromDate());
        assertEquals("2024-08-31", res.getToDate());
    }
}
