package com.moin.demomoin.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.experimental.UtilityClass;


@UtilityClass
public class InstantUtil {

  // LocalDate to Instant
  public Instant toInstant(LocalDate localDate) {
    if (localDate == null) {
      return Instant.now();
    }
    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
  }

  // LocalDateTime to Instant
  public Instant toInstant(LocalDateTime localDateTime) {
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
  }

  // Instant to LocalDate
  public LocalDate toLocalDate(Instant instant) {
    return instant.atZone(ZoneId.systemDefault()).toLocalDate();
  }

  // Instant to LocalDateTime
  public LocalDateTime toLocalDateTime(Instant instant) {
    return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  // Instant to ZonedDateTime
  public ZonedDateTime toZonedDateTime(Instant instant) {
    return instant.atZone(ZoneId.systemDefault());
  }

  // ZonedDateTime to Instant
  public Instant toInstant(ZonedDateTime zonedDateTime) {
    return zonedDateTime.toInstant();
  }

  public static Instant fromLocalDateToStartInstant(LocalDate limitStartPeriod) {
    return ZonedDateTime.of(limitStartPeriod == null ? LocalDate.now() : limitStartPeriod,
            LocalTime.of(0, 0), ZoneId.systemDefault())
        .toInstant();
  }

  public static Instant fromLocalDateToEndInstant(LocalDate limitEndPeriod) {
    return ZonedDateTime.of(limitEndPeriod == null ? LocalDate.now() : limitEndPeriod,
            LocalTime.of(23, 59), ZoneId.systemDefault())
        .toInstant();
  }

  public static Timestamp toTimestamp(LocalDate localDate) {
    if (localDate != null) {
      return Timestamp.valueOf(localDate.atStartOfDay());
    }
    return null;
  }

  public static Timestamp toStartTimestamp(LocalDate localDate) {
    if (localDate != null) {
      // +00:00
      var zonedDateTime = ZonedDateTime.of(localDate, LocalTime.of(0, 0), ZoneId.of("UTC"));
      System.out.println("zonedDateTime = " + zonedDateTime);
      return Timestamp.from(
          zonedDateTime.toInstant());
    }
    return null;
  }

  public static Timestamp toEndTimestamp(LocalDate localDate) {
    if (localDate != null) {
      var zonedDateTime = ZonedDateTime.of(localDate, LocalTime.of(23, 59), ZoneId.of("UTC"));
      System.out.println("zonedDateTime = " + zonedDateTime);
      return Timestamp.from(
          zonedDateTime.toInstant());
    }
    return null;
  }

  public static Instant toStartTimestamp() {
    return ZonedDateTime.of(LocalDate.now(), LocalTime.of(0, 0), ZoneId.systemDefault())
        .toInstant();
  }

  public static Instant toEndTimestamp() {
    return ZonedDateTime.of(LocalDate.now(), LocalTime.of(23, 59), ZoneId.systemDefault())
        .toInstant();
  }

}