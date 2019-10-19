package com.aron.flightschedule.controller;

import com.aron.flightschedule.model.FlightScheduleDataEvent;
import com.aron.flightschedule.service.DataEventProducer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/test/flightschedules")
public class FlightScheduleDataEventTestController {

    private final DataEventProducer dataEventProducer;

    public FlightScheduleDataEventTestController(DataEventProducer dataEventProducer) {
        this.dataEventProducer = dataEventProducer;
    }

    // NOTE: only here to help in testing application to be able to put messages on input topic: 'topicin'
    @RequestMapping(value = "/data-events", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendDataEventTest(@RequestBody FlightScheduleDataEvent flightScheduleDataEvent) {
        flightScheduleDataEvent.setTimestamp(System.currentTimeMillis());
        dataEventProducer.sendDataEvent(flightScheduleDataEvent);
    }

}
