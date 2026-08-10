package com.acme.modres.mbean.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.acme.modres.Constants;
import com.acme.modres.cloud.AzureServiceBusScheduler;

public class ReservationCheckerData {
  private static final DateTimeFormatter RESERVATION_DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATA_FORMAT);

  private ReservationList reservations;
  private LocalDate selectedDate;
  private boolean available;

  public ReservationCheckerData(ReservationList reservations) {
    this.reservations = reservations;
    this.available = true;
  }

  public ReservationList getReservationList() {
    return reservations;
  }

  public LocalDate getSelectedDate() {
    return selectedDate;
  }

  public boolean setSelectedDate(String dateStr) {
    try {
      selectedDate = LocalDate.parse(dateStr, RESERVATION_DATE_FORMATTER);
      AzureServiceBusScheduler.scheduleIfConfigured(
          "{\"operation\":\"reservation-date-selected\",\"date\":\"" + dateStr + "\"}",
          selectedDate.atStartOfDay().atOffset(java.time.ZoneOffset.UTC));
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public boolean isAvailible() {
    return available;
  }

  public void setAvailablility(boolean available) {
    this.available = available;
  }
}
