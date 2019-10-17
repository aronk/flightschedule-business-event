package com.aron.flightschedule.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class FlightScheduleUtils {

    public static boolean isDateTimeOutsideDeltaInMinutes(
            final LocalDateTime existingDateTime,
            final LocalDateTime newDateTime,
            final Long deltaMins) {

        if (existingDateTime == null || newDateTime == null || deltaMins == null) {
            return false;
        }

        LocalDateTime min = existingDateTime.minus(deltaMins, ChronoUnit.MINUTES);
        LocalDateTime max = existingDateTime.plus(deltaMins, ChronoUnit.MINUTES);

        if (newDateTime.isBefore(min) || newDateTime.isAfter(max)) {
            return true;
        }

        return false;
    }

}
