package com.aron.flightschedule.controller;


import com.aron.flightschedule.TestConstants;
import com.aron.flightschedule.config.FlightScheduleConfig;
import com.aron.flightschedule.service.FlightScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.aron.flightschedule.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FlightScheduleController.class)
@Import({FlightScheduleConfig.class})
@Slf4j
@RunWith(SpringRunner.class)
public class FlightScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightScheduleService flightScheduleService;

    @Test
    public void shouldLoadContext() {
    }

    @Test
    public void shouldReturn_notFound_getFlightSchedule_WhenInvalidRequest() throws Exception {
        Long flightScheduleId = 1L;
        String requestUrl = createGetFlightScheduleUrl(flightScheduleId);
        log.debug("requestUrl={}", requestUrl);

        when(flightScheduleService.getFlightSchedule(any(Long.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(get(requestUrl))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("@.timestamp").exists())
                .andExpect(jsonPath("@.status").value(404))
                .andExpect(jsonPath("@.exceptionMessage").value("supplied id not found"))
                .andExpect(jsonPath("@.exceptionClass").isString())
                .andExpect(jsonPath("$.path").isNotEmpty())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn_ok_getFlightSchedule_WhenValidRequest() throws Exception {
        Long flightScheduleId = 12345678L;
        String requestUrl = createGetFlightScheduleUrl(flightScheduleId);
        log.debug("requestUrl={}", requestUrl);

        when(flightScheduleService.getFlightSchedule(any(Long.class))).thenReturn(TestConstants.FLIGHT_SCHEDULE());

        this.mockMvc.perform(get(requestUrl))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("@.id").value(TEST_FLIGHT_SCHEDULE_ID))
                .andExpect(jsonPath("@.flightNumber").value(TEST_FLIGHT_SCHEDULE_FLIGHT_NUMBER))
                .andExpect(jsonPath("@.departureDate").value(TEST_FLIGHT_SCHEDULE_DEPARTURE_DATE.format(LOCAL_DATE_JSON_FORMATTER)))
                .andExpect(jsonPath("@.scheduledDepartureTime").value(TEST_FLIGHT_SCHEDULE_SCHEDULED_DEPARTURE_TIME.format(LOCAL_DATE_TIME_JSON_FORMATTER)))
                .andExpect(jsonPath("@.estimatedDepartureTime").value(TEST_FLIGHT_SCHEDULE_ESTIMATED_DEPARTURE_TIME.format(LOCAL_DATE_TIME_JSON_FORMATTER)))
                .andExpect(jsonPath("@.originPort").value(TEST_FLIGHT_SCHEDULE_ORIGIN_PORT))
                .andExpect(jsonPath("@.originCountry").value(TEST_FLIGHT_SCHEDULE_ORIGIN_COUNTRY))
                .andExpect(jsonPath("@.destinationPort").value(TEST_FLIGHT_SCHEDULE_DESTINATION_PORT))
                .andExpect(jsonPath("@.destinationCountry").value(TEST_FLIGHT_SCHEDULE_DESTINATION_COUNTRY))
                .andExpect(jsonPath("@.airline").value(TEST_FLIGHT_SCHEDULE_AIRLINE))
                .andExpect(jsonPath("@.status").value(TEST_FLIGHT_SCHEDULE_STATUS.name()))
                .andExpect(status().isOk());
    }

}