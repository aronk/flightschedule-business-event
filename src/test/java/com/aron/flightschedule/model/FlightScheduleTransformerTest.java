package com.aron.flightschedule.model;

import com.aron.flightschedule.TestConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static com.aron.flightschedule.TestConstants.*;
import static com.aron.flightschedule.model.FlightSchedule.FlightScheduleStatus.SCHEDULED;
import static com.aron.flightschedule.model.FlightScheduleBusinessEvent.BusinessEventType.FLIGHT_DELAYED;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
public class FlightScheduleTransformerTest {

    private FlightScheduleTransformer flightScheduleTransformer;

    @Before
    public void setup() {
        flightScheduleTransformer = new FlightScheduleTransformer();
    }

    @Test
    public void test_fromFlightScheduleDataEvent_WhenNoAttributesSet() {
        FlightScheduleDataEvent flightScheduleDataEvent = FlightScheduleDataEvent.builder().build();

        FlightSchedule flightSchedule = flightScheduleTransformer.fromFlightScheduleDataEvent(flightScheduleDataEvent);
        log.debug("flightSchedule={}", flightSchedule);

        assertThat(flightSchedule).isNotNull();
        assertThat(flightSchedule.getId()).isNull();
        assertThat(flightSchedule.getFlightNumber()).isNull();
        assertThat(flightSchedule.getDepartureDate()).isNull();
        assertThat(flightSchedule.getScheduledDepartureTime()).isNull();
        assertThat(flightSchedule.getEstimatedDepartureTime()).isNull();
        assertThat(flightSchedule.getOriginPort()).isNull();
        assertThat(flightSchedule.getOriginCountry()).isNull();
        assertThat(flightSchedule.getDestinationPort()).isNull();
        assertThat(flightSchedule.getDestinationCountry()).isNull();
        assertThat(flightSchedule.getAirline()).isNull();
        assertThat(flightSchedule.getStatus()).isEqualTo(SCHEDULED);
    }

    @Test
    public void test_fromFlightScheduleDataEvent_WhenAllAttributesSet() {
        FlightScheduleDataEvent flightScheduleDataEvent = FlightScheduleDataEvent.builder()
                .flightScheduleId(TEST_FLIGHT_SCHEDULE_ID)
                .flightNumber(TEST_FLIGHT_SCHEDULE_FLIGHT_NUMBER)
                .departureDate(TEST_FLIGHT_SCHEDULE_DEPARTURE_DATE)
                .scheduledDepartureTime(TEST_FLIGHT_SCHEDULE_SCHEDULED_DEPARTURE_TIME)
                .estimatedDepartureTime(TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME)
                .originPort(TEST_FLIGHT_SCHEDULE_ORIGIN_PORT)
                .originCountry(TEST_FLIGHT_SCHEDULE_ORIGIN_COUNTRY)
                .destinationPort(TEST_FLIGHT_SCHEDULE_DESTINATION_PORT)
                .destinationCountry(TEST_FLIGHT_SCHEDULE_DESTINATION_COUNTRY)
                .airline(TEST_FLIGHT_SCHEDULE_AIRLINE)
                .timestamp(System.currentTimeMillis())
                .build();

        FlightSchedule flightSchedule = flightScheduleTransformer.fromFlightScheduleDataEvent(flightScheduleDataEvent);
        log.debug("flightSchedule={}", flightSchedule);

        assertThat(flightSchedule).isNotNull();
        assertThat(flightSchedule.getId()).isEqualTo(TEST_FLIGHT_SCHEDULE_ID);
        assertThat(flightSchedule.getFlightNumber()).isEqualTo(TEST_FLIGHT_SCHEDULE_FLIGHT_NUMBER);
        assertThat(flightSchedule.getDepartureDate()).isEqualTo(TEST_FLIGHT_SCHEDULE_DEPARTURE_DATE);
        assertThat(flightSchedule.getScheduledDepartureTime()).isEqualTo(TEST_FLIGHT_SCHEDULE_SCHEDULED_DEPARTURE_TIME);
        assertThat(flightSchedule.getEstimatedDepartureTime()).isEqualTo(TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME);
        assertThat(flightSchedule.getOriginPort()).isEqualTo(TEST_FLIGHT_SCHEDULE_ORIGIN_PORT);
        assertThat(flightSchedule.getOriginCountry()).isEqualTo(TEST_FLIGHT_SCHEDULE_ORIGIN_COUNTRY);
        assertThat(flightSchedule.getDestinationPort()).isEqualTo(TEST_FLIGHT_SCHEDULE_DESTINATION_PORT);
        assertThat(flightSchedule.getDestinationCountry()).isEqualTo(TEST_FLIGHT_SCHEDULE_DESTINATION_COUNTRY);
        assertThat(flightSchedule.getAirline()).isEqualTo(TEST_FLIGHT_SCHEDULE_AIRLINE);
        assertThat(flightSchedule.getStatus()).isEqualTo(SCHEDULED);
    }

    @Test
    public void test_toFlightDelayedBusinessEvent_WhenNoAttributesSet() {
        FlightSchedule flightSchedule = FlightSchedule.builder().build();

        FlightScheduleBusinessEvent flightScheduleBusinessEvent = flightScheduleTransformer.toFlightDelayedBusinessEvent(flightSchedule);
        log.debug("flightScheduleBusinessEvent={}", flightScheduleBusinessEvent);

        assertThat(flightScheduleBusinessEvent).isNotNull();
        assertThat(flightScheduleBusinessEvent.getEventType()).isEqualTo(FLIGHT_DELAYED.name());
        assertThat(flightScheduleBusinessEvent.getFlightDelayed()).isNotNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed()).isNotNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getFlightScheduleId()).isNull();

        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getFlightNumber()).isNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getDepartureDate()).isNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getScheduledDepartureTime()).isNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getEstimatedDepartureTime()).isNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getOriginPort()).isNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getOriginCountry()).isNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getDestinationPort()).isNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getDestinationCountry()).isNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getAirline()).isNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getFlightScheduleStatus()).isNull();
    }

    @Test
    public void test_toFlightDelayedBusinessEvent_WhenAllAttributesSet() {
        FlightSchedule flightSchedule = TestConstants.FLIGHT_SCHEDULE().get();

        FlightScheduleBusinessEvent flightScheduleBusinessEvent = flightScheduleTransformer.toFlightDelayedBusinessEvent(flightSchedule);
        log.debug("flightScheduleBusinessEvent={}", flightScheduleBusinessEvent);

        assertThat(flightScheduleBusinessEvent).isNotNull();
        assertThat(flightScheduleBusinessEvent.getEventType()).isEqualTo(FLIGHT_DELAYED.name());
        assertThat(flightScheduleBusinessEvent.getFlightDelayed()).isNotNull();
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getFlightScheduleId()).isEqualTo(TEST_FLIGHT_SCHEDULE_ID);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getFlightNumber()).isEqualTo(TEST_FLIGHT_SCHEDULE_FLIGHT_NUMBER);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getDepartureDate()).isEqualTo(TEST_FLIGHT_SCHEDULE_DEPARTURE_DATE);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getScheduledDepartureTime()).isEqualTo(TEST_FLIGHT_SCHEDULE_SCHEDULED_DEPARTURE_TIME);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getEstimatedDepartureTime()).isEqualTo(TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getOriginPort()).isEqualTo(TEST_FLIGHT_SCHEDULE_ORIGIN_PORT);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getOriginCountry()).isEqualTo(TEST_FLIGHT_SCHEDULE_ORIGIN_COUNTRY);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getDestinationPort()).isEqualTo(TEST_FLIGHT_SCHEDULE_DESTINATION_PORT);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getDestinationCountry()).isEqualTo(TEST_FLIGHT_SCHEDULE_DESTINATION_COUNTRY);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getAirline()).isEqualTo(TEST_FLIGHT_SCHEDULE_AIRLINE);
        assertThat(flightScheduleBusinessEvent.getFlightDelayed().getFlightScheduleStatus()).isEqualTo(TEST_FLIGHT_SCHEDULE_STATUS.name());
    }

}