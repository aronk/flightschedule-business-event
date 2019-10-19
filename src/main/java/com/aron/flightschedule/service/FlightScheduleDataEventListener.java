package com.aron.flightschedule.service;

import com.aron.flightschedule.model.FlightScheduleDataEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlightScheduleDataEventListener {

    private final FlightScheduleService flightScheduleService;

    public FlightScheduleDataEventListener(FlightScheduleService flightScheduleService) {
        this.flightScheduleService = flightScheduleService;
    }

    @StreamListener(FlightScheduleStreams.TOPICIN_SUB)
    public void handleFlightScheduleDataEvent(@Payload FlightScheduleDataEvent flightScheduleDataEvent) {
        log.debug("Received flightScheduleDataEvent: {}", flightScheduleDataEvent);

        if (flightScheduleDataEvent == null) {
            log.error("handleFlightScheduleDataEvent(): not processing message, flightScheduleDataEvent={}", flightScheduleDataEvent);
            return;
        }

        flightScheduleService.processFlightScheduleDataEvent(flightScheduleDataEvent);

    }
}
