package com.aron.flightschedule.service;

import com.aron.flightschedule.model.FlightScheduleBusinessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlightScheduleBusinessEventListener {

    @StreamListener(FlightScheduleStreams.TOPICOUT_SUB)
    public void handleGreetings(@Payload FlightScheduleBusinessEvent flightScheduleBusinessEvent) {
        log.info("Received flightScheduleBusinessEvent: {}", flightScheduleBusinessEvent);
    }

}
