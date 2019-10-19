package com.aron.flightschedule;

import com.aron.flightschedule.model.FlightSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.aron.flightschedule.model.FlightSchedule.FlightScheduleStatus.SCHEDULED;

public class TestConstants {

    public static DateTimeFormatter LOCAL_DATE_JSON_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter LOCAL_DATE_TIME_JSON_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static final long TEST_FLIGHT_SCHEDULE_ID = 12345678L;
    public static final String TEST_FLIGHT_SCHEDULE_FLIGHT_NUMBER = "QF425";
    public static final LocalDate TEST_FLIGHT_SCHEDULE_DEPARTURE_DATE = LocalDate.of(2019, 10, 16);
    public static final LocalDateTime TEST_FLIGHT_SCHEDULE_SCHEDULED_DEPARTURE_TIME = LocalDateTime.of(2019, 10, 16, 6, 10, 39);
    public static final LocalDateTime TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME = LocalDateTime.of(2019, 10, 16, 6, 31, 00);
    public static final String TEST_FLIGHT_SCHEDULE_ORIGIN_PORT = "Sydney";
    public static final String TEST_FLIGHT_SCHEDULE_ORIGIN_COUNTRY = "Australia";
    public static final String TEST_FLIGHT_SCHEDULE_DESTINATION_PORT = "Melbourne";
    public static final String TEST_FLIGHT_SCHEDULE_DESTINATION_COUNTRY = "Australia";
    public static final String TEST_FLIGHT_SCHEDULE_AIRLINE = "QF";
    public static final FlightSchedule.FlightScheduleStatus TEST_FLIGHT_SCHEDULE_STATUS = SCHEDULED;

    public static final String URL_FLIGHT_SCHEDULE_GET = "/flightschedules/{id}";

    public static String createGetFlightScheduleUrl(Long flightScheduleId) {
        String url = URL_FLIGHT_SCHEDULE_GET.replace("{id}", flightScheduleId.toString());
        return url;
    }

    public static Optional<FlightSchedule> FLIGHT_SCHEDULE() {
        return Optional.of(FlightSchedule.builder()
                .id(TEST_FLIGHT_SCHEDULE_ID)
                .flightNumber(TEST_FLIGHT_SCHEDULE_FLIGHT_NUMBER)
                .departureDate(TEST_FLIGHT_SCHEDULE_DEPARTURE_DATE)
                .scheduledDepartureTime(TEST_FLIGHT_SCHEDULE_SCHEDULED_DEPARTURE_TIME)
                .estimatedDepartureTime(TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME)
                .originPort(TEST_FLIGHT_SCHEDULE_ORIGIN_PORT)
                .originCountry(TEST_FLIGHT_SCHEDULE_ORIGIN_COUNTRY)
                .destinationPort(TEST_FLIGHT_SCHEDULE_DESTINATION_PORT)
                .destinationCountry(TEST_FLIGHT_SCHEDULE_DESTINATION_COUNTRY)
                .airline(TEST_FLIGHT_SCHEDULE_AIRLINE)
                .status(TEST_FLIGHT_SCHEDULE_STATUS)
                .build());
    }
}
