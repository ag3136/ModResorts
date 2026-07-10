package com.acme.modres.mbean.reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.acme.modres.Constants;

/**
 * Date availability checker that standardizes all date parsing on UTC (blocker-12, blocker-13).
 *
 * Replaced server-local timezone-dependent SimpleDateFormat with UTC-explicit parsing
 * to ensure consistent behavior across distributed cloud deployments in multiple regions.
 */
public class DateChecker implements Runnable {
  ReservationCheckerData data;
  List<Reservation> reservations;

  public DateChecker(ReservationCheckerData data) {
    this.data = data;
    this.reservations = data.getReservationList().getReservations();
  }

  public void run() {
    for (int i = 0; i < reservations.size(); i++) {
      Reservation reservation = reservations.get(i);
      Date selectedDate = data.getSelectedDate();

      try {
        // Standardize on UTC to eliminate server-local timezone dependencies (blocker-12, blocker-13)
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATA_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date fromDate = sdf.parse(reservation.getFromDate());
        Date toDate = sdf.parse(reservation.getToDate());
        if (selectedDate.after(fromDate) && selectedDate.before(toDate)) {
          data.setAvailablility(false);
          break;
        }
      } catch (ParseException ex) {
        ex.printStackTrace();
      }
    }
    data.setAvailablility(true);
  }
}
