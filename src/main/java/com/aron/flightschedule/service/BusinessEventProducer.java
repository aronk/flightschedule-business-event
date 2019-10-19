package com.aron.flightschedule.service;

import com.aron.flightschedule.model.FlightScheduleBusinessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component
@Slf4j
public class BusinessEventProducer {

    private final FlightScheduleStreams flightScheduleStreams;

    public BusinessEventProducer(FlightScheduleStreams flightScheduleStreams) {
        this.flightScheduleStreams = flightScheduleStreams;
        log.info("BusinessEventProducer(): flightScheduleStreams={}", flightScheduleStreams);
    }

    public void sendBusinessEvent(final FlightScheduleBusinessEvent flightScheduleBusinessEvent) {
        log.debug("Sending  flightScheduleBusinessEvent: {}", flightScheduleBusinessEvent);

        flightScheduleStreams.topicOutPublish()
                .send(MessageBuilder.withPayload(flightScheduleBusinessEvent)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build());
    }

}
