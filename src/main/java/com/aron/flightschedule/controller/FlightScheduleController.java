package com.aron.flightschedule.controller;

import com.aron.flightschedule.common.ResourceNotFoundException;
import com.aron.flightschedule.model.FlightSchedule;
import com.aron.flightschedule.model.FlightScheduleDataEvent;
import com.aron.flightschedule.service.FlightScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/flightschedules")
@Slf4j
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    public FlightScheduleController(FlightScheduleService flightScheduleService) {
        this.flightScheduleService = flightScheduleService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public FlightSchedule getFlightSchedule(@PathVariable("id") final Long id) {
        return flightScheduleService.getFlightSchedule(id).orElseThrow(() -> new ResourceNotFoundException("supplied id not found"));
    }

    // FIXME :
//    @RequestMapping(value = "/{id}/status", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)

    // NOTE: only here to help in testing application to be able to put messages on topicin
    @RequestMapping(value = "/data-events", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendDataEventTest(@RequestBody FlightScheduleDataEvent flightScheduleDataEvent) {

        flightScheduleDataEvent.setTimestamp(System.currentTimeMillis());

        flightScheduleService.sendDataEventTest(flightScheduleDataEvent);
    }

}
