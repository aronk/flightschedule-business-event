package com.aron.flightschedule.service;

import com.aron.flightschedule.model.FlightSchedule;
import com.aron.flightschedule.model.FlightScheduleBusinessEvent;
import com.aron.flightschedule.model.FlightScheduleDataEvent;
import com.aron.flightschedule.model.FlightScheduleTransformer;
import com.aron.flightschedule.repository.FlightScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.aron.flightschedule.TestConstants.TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME;
import static com.aron.flightschedule.TestConstants.TEST_FLIGHT_SCHEDULE_ID;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class FlightScheduleServiceTest {

    private FlightScheduleService flightScheduleService;

    @Mock
    private FlightScheduleRepository flightScheduleRepository;

    @Mock
    private FlightScheduleTransformer flightScheduleTransformer;

    @Mock
    private BusinessEventProducer businessEventProducer;

    private Long estimatedDepartureTimeDeltaMins = 15L;

    @Before
    public void setup() {
        flightScheduleService = new FlightScheduleService(flightScheduleRepository, flightScheduleTransformer, businessEventProducer, estimatedDepartureTimeDeltaMins);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(flightScheduleRepository, flightScheduleTransformer, businessEventProducer);
    }

    @Test
    public void test_processFlightScheduleDataEvent_whenNoExistingFlightScheduleExists() {
        // given
        FlightScheduleDataEvent flightScheduleDataEvent = null;
        Long id = TEST_FLIGHT_SCHEDULE_ID;
        FlightSchedule newFlightSchedule = FlightSchedule.builder().id(id).build();
        Optional<FlightSchedule> existingFlightSchedule = empty();

        given(flightScheduleTransformer.fromFlightScheduleDataEvent(flightScheduleDataEvent)).willReturn(newFlightSchedule);
        given(flightScheduleRepository.findById(id)).willReturn(existingFlightSchedule);
        given(flightScheduleRepository.save(newFlightSchedule)).willReturn(null);

        // when
        flightScheduleService.processFlightScheduleDataEvent(flightScheduleDataEvent);

        // then
        verify(flightScheduleTransformer).fromFlightScheduleDataEvent(flightScheduleDataEvent);
        verify(flightScheduleRepository).findById(id);
        verify(flightScheduleRepository).save(newFlightSchedule);
    }

    @Test
    public void test_processFlightScheduleDataEvent_wheFlightScheduleExistsWithEstimatedDepartureTimeWithinTolerance() {
        // given
        FlightScheduleDataEvent flightScheduleDataEvent = null;
        Long id = TEST_FLIGHT_SCHEDULE_ID;
        FlightSchedule newFlightSchedule = FlightSchedule.builder()
                .id(id)
                .estimatedDepartureTime(TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME)
                .build();
        Optional<FlightSchedule> existingFlightSchedule = of(FlightSchedule.builder()
                .estimatedDepartureTime(TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME
                        .plus(estimatedDepartureTimeDeltaMins, MINUTES))
                .build());

        given(flightScheduleTransformer.fromFlightScheduleDataEvent(flightScheduleDataEvent)).willReturn(newFlightSchedule);
        given(flightScheduleRepository.findById(id)).willReturn(existingFlightSchedule);
        given(flightScheduleRepository.save(newFlightSchedule)).willReturn(null);

        // when
        flightScheduleService.processFlightScheduleDataEvent(flightScheduleDataEvent);

        // then
        verify(flightScheduleTransformer).fromFlightScheduleDataEvent(flightScheduleDataEvent);
        verify(flightScheduleRepository).findById(id);
        verify(flightScheduleRepository).save(newFlightSchedule);
    }

    @Test
    public void test_processFlightScheduleDataEvent_wheFlightScheduleExistsWithEstimatedDepartureTimeOutsideTolerance() {
        // given
        FlightScheduleDataEvent flightScheduleDataEvent = null;
        Long id = TEST_FLIGHT_SCHEDULE_ID;
        FlightSchedule newFlightSchedule = FlightSchedule.builder()
                .id(id)
                .estimatedDepartureTime(TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME)
                .build();
        Optional<FlightSchedule> existingFlightSchedule = of(FlightSchedule.builder()
                .estimatedDepartureTime(TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME
                        .plus(estimatedDepartureTimeDeltaMins, MINUTES)
                        .plus(1L, ChronoUnit.MILLIS))
                .build());
        FlightScheduleBusinessEvent flightScheduleBusinessEvent = FlightScheduleBusinessEvent.builder().build();

        given(flightScheduleTransformer.fromFlightScheduleDataEvent(flightScheduleDataEvent)).willReturn(newFlightSchedule);
        given(flightScheduleRepository.findById(id)).willReturn(existingFlightSchedule);
        given(flightScheduleRepository.save(newFlightSchedule)).willReturn(null);
        given(flightScheduleTransformer.toFlightDelayedBusinessEvent(newFlightSchedule)).willReturn(flightScheduleBusinessEvent);
        doNothing().when(businessEventProducer).sendBusinessEvent(flightScheduleBusinessEvent);

        // when
        flightScheduleService.processFlightScheduleDataEvent(flightScheduleDataEvent);

        // then
        verify(flightScheduleTransformer).fromFlightScheduleDataEvent(flightScheduleDataEvent);
        verify(flightScheduleRepository).findById(id);
        verify(flightScheduleRepository).save(newFlightSchedule);
        verify(flightScheduleTransformer).toFlightDelayedBusinessEvent(newFlightSchedule);
        verify(businessEventProducer).sendBusinessEvent(flightScheduleBusinessEvent);
    }

}