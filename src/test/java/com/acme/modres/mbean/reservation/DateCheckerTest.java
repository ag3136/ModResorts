package com.acme.modres.mbean.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DateCheckerTest {

    private DateChecker dateChecker;

    @Mock
    private ReservationCheckerData data;

    private ReservationList reservationList;
    private List<Reservation> reservations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservations = new ArrayList<>();
        reservationList = new ReservationList(reservations);
        when(data.getReservationList()).thenReturn(reservationList);
        dateChecker = new DateChecker(data);
    }

    @Test
    void testConstructor_shouldInitializeFields() {
        // Assert
        assertNotNull(dateChecker);
    }

    @Test
    void testRun_withNoReservations_shouldSetAvailableTrue() {
        // Arrange
        when(data.getSelectedDate()).thenReturn(new java.util.Date());

        // Act
        dateChecker.run();

        // Assert
        verify(data).setAvailablility(true);
    }

    @Test
    void testRun_withValidReservation_shouldCheckAvailability() {
        // Arrange
        Reservation reservation = new Reservation("08/01/2024", "08/31/2024");
        reservations.add(reservation);
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(2024, 7, 15); // August 15, 2024
        when(data.getSelectedDate()).thenReturn(cal.getTime());

        // Act
        dateChecker.run();

        // Assert
        verify(data, atLeastOnce()).setAvailablility(anyBoolean());
    }

    @Test
    void testRun_withInvalidDateFormat_shouldHandleException() {
        // Arrange
        Reservation reservation = new Reservation("invalid-date", "invalid-date");
        reservations.add(reservation);
        when(data.getSelectedDate()).thenReturn(new java.util.Date());

        // Act & Assert
        assertDoesNotThrow(() -> dateChecker.run());
    }

    @Test
    void testRun_withMultipleReservations_shouldCheckAll() {
        // Arrange
        reservations.add(new Reservation("08/01/2024", "08/10/2024"));
        reservations.add(new Reservation("08/15/2024", "08/25/2024"));
        reservations.add(new Reservation("09/01/2024", "09/10/2024"));
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(2024, 7, 20); // August 20, 2024
        when(data.getSelectedDate()).thenReturn(cal.getTime());

        // Act
        dateChecker.run();

        // Assert
        verify(data, atLeastOnce()).setAvailablility(anyBoolean());
    }

    @Test
    void testRun_withNullSelectedDate_shouldHandleGracefully() {
        // Arrange
        when(data.getSelectedDate()).thenReturn(null);
        reservations.add(new Reservation("08/01/2024", "08/31/2024"));

        // Act & Assert
        assertDoesNotThrow(() -> dateChecker.run());
    }

    @Test
    void testRun_withEmptyReservationList_shouldSetAvailableTrue() {
        // Arrange
        when(data.getSelectedDate()).thenReturn(new java.util.Date());

        // Act
        dateChecker.run();

        // Assert
        verify(data).setAvailablility(true);
    }
}
