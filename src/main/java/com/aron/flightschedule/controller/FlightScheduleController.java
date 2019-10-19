package com.aron.flightschedule.controller;

import com.aron.flightschedule.common.ResourceNotFoundException;
import com.aron.flightschedule.model.FlightSchedule;
import com.aron.flightschedule.service.FlightScheduleService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/flightschedules")
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    public FlightScheduleController(FlightScheduleService flightScheduleService) {
        this.flightScheduleService = flightScheduleService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public FlightSchedule getFlightSchedule(@PathVariable("id") final Long id) {
        return flightScheduleService.getFlightSchedule(id).orElseThrow(() -> new ResourceNotFoundException("supplied id not found"));
    }

}
