package com.aron.flightschedule.service;

import com.aron.flightschedule.model.FlightScheduleDataEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component
@Slf4j
public class DataEventProducer {

    private final FlightScheduleStreams flightScheduleStreams;

    public DataEventProducer(FlightScheduleStreams flightScheduleStreams) {
        this.flightScheduleStreams = flightScheduleStreams;
        log.info("DataEventProducer(): flightScheduleStreams={}", flightScheduleStreams);
    }

    public void sendDataEvent(final FlightScheduleDataEvent flightScheduleDataEvent) {
        log.debug("Sending  flightScheduleDataEvent: {}", flightScheduleDataEvent);

        flightScheduleStreams.topicInPublish()
                .send(MessageBuilder.withPayload(flightScheduleDataEvent)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build());
    }

}
