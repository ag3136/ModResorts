package com.acme.modres.mbean.reservation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.acme.modres.Constants;

/**
 * Reservation checker data model with UTC-standardized date parsing (blocker-14).
 *
 * Replaced server-local timezone-dependent SimpleDateFormat with UTC-explicit parsing
 * to ensure consistent scheduling behavior across distributed cloud deployments.
 */
public class ReservationCheckerData {
  private ReservationList reservations;
  private Date selectedDate;
  private boolean available; // changed from Boolean to boolean

  public ReservationCheckerData(ReservationList reservations) {
    this.reservations = reservations;
    this.available = true;
  }

  public ReservationList getReservationList() {
    return reservations;
  }

  public Date getSelectedDate() {
    return selectedDate;
  }

  /**
   * Parses the date string using UTC timezone to avoid server-local clock/timezone
   * dependencies in distributed cloud environments (blocker-14).
   *
   * @param dateStr date string in the application's configured format
   * @return true if parsing succeeded, false otherwise
   */
  public boolean setSelectedDate(String dateStr) {
    try {
      // Standardize on UTC to eliminate server-local timezone dependencies (blocker-14)
      SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATA_FORMAT);
      sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
      selectedDate = sdf.parse(dateStr);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public boolean isAvailible() {
    return available;
  }

  public void setAvailablility(boolean available) { // fix parameter type
    this.available = available;
  }
}
