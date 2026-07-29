package com.acme.modres.mbean.reservation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationListTest {

    private ReservationList reservationList;

    @BeforeEach
    void setUp() {
        reservationList = new ReservationList();
    }

    @Test
    void testDefaultConstructor_shouldCreateInstance() {
        // Assert
        assertNotNull(reservationList);
        assertNotNull(reservationList.getReservations());
        assertTrue(reservationList.getReservations().isEmpty());
    }

    @Test
    void testParameterizedConstructor_shouldSetReservations() {
        // Arrange
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation("08/01/2024", "08/31/2024"));
        reservations.add(new Reservation("09/01/2024", "09/30/2024"));

        // Act
        ReservationList list = new ReservationList(reservations);

        // Assert
        assertEquals(2, list.getReservations().size());
        assertEquals(reservations, list.getReservations());
    }

    @Test
    void testAdd_shouldAddReservation() {
        // Arrange
        Reservation reservation = new Reservation("08/01/2024", "08/31/2024");

        // Act
        reservationList.add(reservation);

        // Assert
        assertEquals(1, reservationList.getReservations().size());
        assertEquals(reservation, reservationList.getReservations().get(0));
    }

    @Test
    void testAdd_multipleReservations_shouldAddAll() {
        // Arrange
        Reservation res1 = new Reservation("08/01/2024", "08/31/2024");
        Reservation res2 = new Reservation("09/01/2024", "09/30/2024");
        Reservation res3 = new Reservation("10/01/2024", "10/31/2024");

        // Act
        reservationList.add(res1);
        reservationList.add(res2);
        reservationList.add(res3);

        // Assert
        assertEquals(3, reservationList.getReservations().size());
    }

    @Test
    void testGetReservations_shouldReturnList() {
        // Act
        List<Reservation> result = reservationList.getReservations();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof List);
    }

    @Test
    void testAdd_withNullReservation_shouldAddNull() {
        // Act
        reservationList.add(null);

        // Assert
        assertEquals(1, reservationList.getReservations().size());
        assertNull(reservationList.getReservations().get(0));
    }

    @Test
    void testParameterizedConstructor_withEmptyList_shouldCreateEmptyList() {
        // Arrange
        List<Reservation> emptyList = new ArrayList<>();

        // Act
        ReservationList list = new ReservationList(emptyList);

        // Assert
        assertTrue(list.getReservations().isEmpty());
    }

    @Test
    void testParameterizedConstructor_withNullList_shouldHandleGracefully() {
        // Act
        ReservationList list = new ReservationList(null);

        // Assert
        assertNotNull(list);
        assertNull(list.getReservations());
    }

    @Test
    void testGetReservations_shouldReturnMutableList() {
        // Act
        List<Reservation> list = reservationList.getReservations();
        Reservation reservation = new Reservation("08/01/2024", "08/31/2024");
        list.add(reservation);

        // Assert
        assertEquals(1, reservationList.getReservations().size());
    }

    @Test
    void testAdd_sameReservationMultipleTimes_shouldAddMultipleTimes() {
        // Arrange
        Reservation reservation = new Reservation("08/01/2024", "08/31/2024");

        // Act
        reservationList.add(reservation);
        reservationList.add(reservation);
        reservationList.add(reservation);

        // Assert
        assertEquals(3, reservationList.getReservations().size());
    }

    @Test
    void testAdd_afterParameterizedConstructor_shouldAddToExistingList() {
        // Arrange
        List<Reservation> initialList = new ArrayList<>();
        initialList.add(new Reservation("08/01/2024", "08/31/2024"));
        ReservationList list = new ReservationList(initialList);
        Reservation newReservation = new Reservation("09/01/2024", "09/30/2024");

        // Act
        list.add(newReservation);

        // Assert
        assertEquals(2, list.getReservations().size());
    }
}
