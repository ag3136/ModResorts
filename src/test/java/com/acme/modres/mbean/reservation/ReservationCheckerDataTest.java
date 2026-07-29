package com.acme.modres.mbean.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReservationCheckerDataTest {

    private ReservationCheckerData checkerData;

    @Mock
    private ReservationList reservationList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        checkerData = new ReservationCheckerData(reservationList);
    }

    @Test
    void testConstructor_shouldInitializeFields() {
        // Assert
        assertNotNull(checkerData);
        assertEquals(reservationList, checkerData.getReservationList());
        assertTrue(checkerData.isAvailible());
    }

    @Test
    void testGetReservationList_shouldReturnReservationList() {
        // Act
        ReservationList result = checkerData.getReservationList();

        // Assert
        assertEquals(reservationList, result);
    }

    @Test
    void testSetSelectedDate_withValidDate_shouldReturnTrue() {
        // Act
        boolean result = checkerData.setSelectedDate("08/15/2024");

        // Assert
        assertTrue(result);
        assertNotNull(checkerData.getSelectedDate());
    }

    @Test
    void testSetSelectedDate_withInvalidDate_shouldReturnFalse() {
        // Act
        boolean result = checkerData.setSelectedDate("invalid-date");

        // Assert
        assertFalse(result);
    }

    @Test
    void testSetSelectedDate_withNull_shouldReturnFalse() {
        // Act
        boolean result = checkerData.setSelectedDate(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void testSetSelectedDate_withEmptyString_shouldReturnFalse() {
        // Act
        boolean result = checkerData.setSelectedDate("");

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetSelectedDate_withoutSetting_shouldReturnNull() {
        // Act
        java.util.Date result = checkerData.getSelectedDate();

        // Assert
        assertNull(result);
    }

    @Test
    void testIsAvailible_defaultValue_shouldReturnTrue() {
        // Act
        boolean result = checkerData.isAvailible();

        // Assert
        assertTrue(result);
    }

    @Test
    void testSetAvailablility_withTrue_shouldSetTrue() {
        // Act
        checkerData.setAvailablility(true);

        // Assert
        assertTrue(checkerData.isAvailible());
    }

    @Test
    void testSetAvailablility_withFalse_shouldSetFalse() {
        // Act
        checkerData.setAvailablility(false);

        // Assert
        assertFalse(checkerData.isAvailible());
    }

    @Test
    void testSetSelectedDate_withDifferentFormats_shouldHandleCorrectly() {
        // Act
        boolean result1 = checkerData.setSelectedDate("12/31/2024");
        boolean result2 = checkerData.setSelectedDate("01/01/2025");

        // Assert
        assertTrue(result1);
        assertTrue(result2);
    }

    @Test
    void testSetAvailablility_multipleTimes_shouldUpdateValue() {
        // Act
        checkerData.setAvailablility(false);
        assertFalse(checkerData.isAvailible());
        
        checkerData.setAvailablility(true);
        assertTrue(checkerData.isAvailible());
        
        checkerData.setAvailablility(false);
        assertFalse(checkerData.isAvailible());
    }

    @Test
    void testConstructor_withNullReservationList_shouldHandleGracefully() {
        // Act
        ReservationCheckerData data = new ReservationCheckerData(null);

        // Assert
        assertNotNull(data);
        assertNull(data.getReservationList());
    }

    @Test
    void testSetSelectedDate_withPastDate_shouldReturnTrue() {
        // Act
        boolean result = checkerData.setSelectedDate("01/01/2020");

        // Assert
        assertTrue(result);
    }

    @Test
    void testSetSelectedDate_withFutureDate_shouldReturnTrue() {
        // Act
        boolean result = checkerData.setSelectedDate("12/31/2030");

        // Assert
        assertTrue(result);
    }
}
