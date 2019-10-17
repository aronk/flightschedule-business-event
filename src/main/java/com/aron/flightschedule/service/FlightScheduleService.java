package com.aron.flightschedule.service;

import com.aron.flightschedule.model.FlightSchedule;
import com.aron.flightschedule.model.FlightScheduleBusinessEvent;
import com.aron.flightschedule.model.FlightScheduleDataEvent;
import com.aron.flightschedule.model.FlightScheduleTransformer;
import com.aron.flightschedule.repository.FlightScheduleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.Optional;

import static com.aron.flightschedule.util.FlightScheduleUtils.isDateTimeOutsideDeltaInMinutes;

@Service
@Slf4j
public class FlightScheduleService {

    private final FlightScheduleStreams flightScheduleStreams;
    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightScheduleTransformer flightScheduleTransformer;
    private final Long estimatedDepartureTimeDeltaMins;

    public FlightScheduleService(FlightScheduleStreams flightScheduleStreams,
                                 FlightScheduleRepository flightScheduleRepository,
                                 FlightScheduleTransformer flightScheduleTransformer,
                                 @Value("${fs.app.estimatedDepartureTimeDeltaMins:15}") Long estimatedDepartureTimeDeltaMins,
                                 ObjectMapper objectMapper) {
        this.flightScheduleStreams = flightScheduleStreams;
        this.flightScheduleRepository = flightScheduleRepository;
        this.flightScheduleTransformer = flightScheduleTransformer;
        this.estimatedDepartureTimeDeltaMins = estimatedDepartureTimeDeltaMins;
        log.info("flightScheduleStreams={}, flightScheduleRepository={}, flightScheduleTransformer={}, estimatedDepartureTimeDeltaMins={}, objectMapper={}",
                flightScheduleStreams, flightScheduleRepository, flightScheduleTransformer, estimatedDepartureTimeDeltaMins, objectMapper);
    }

    public void sendDataEventTest(final FlightScheduleDataEvent flightScheduleDataEvent) {
        log.info("Sending  flightScheduleDataEvent: {}", flightScheduleDataEvent);

        MessageChannel messageChannel = flightScheduleStreams.topicInPublish();
        messageChannel.send(MessageBuilder.withPayload(flightScheduleDataEvent)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    public void processFlightScheduleDataEvent(FlightScheduleDataEvent flightScheduleDataEvent) {

        FlightSchedule newFlightSchedule = flightScheduleTransformer.fromFlightScheduleDataEvent(flightScheduleDataEvent);

        boolean isFlightDelayed = flightScheduleRepository.findById(newFlightSchedule.getId())
                .map(existingFlightSchedule -> isDateTimeOutsideDeltaInMinutes(existingFlightSchedule.getEstimatedDepartureTime(),
                        newFlightSchedule.getEstimatedDepartureTime(), estimatedDepartureTimeDeltaMins))
                .orElse(false);

        if (isFlightDelayed) {
            newFlightSchedule.setStatus(FlightSchedule.FlightScheduleStatus.DELAYED);
        }

        FlightSchedule newFlightScheduleSaved = flightScheduleRepository.save(newFlightSchedule);
        log.debug("newFlightScheduleSaved={}", newFlightScheduleSaved);

        if (isFlightDelayed) {

            FlightScheduleBusinessEvent flightScheduleBusinessEvent = flightScheduleTransformer.toFlightDelayedBusinessEvent(newFlightSchedule);
            log.info("Sending  flightScheduleBusinessEvent: {}", flightScheduleBusinessEvent);

            flightScheduleStreams.topicOutPublish()
                    .send(MessageBuilder.withPayload(flightScheduleBusinessEvent)
                            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                            .build());
        }

    }

    public Optional<FlightSchedule> getFlightSchedule(Long id) {
        return flightScheduleRepository.findById(id);
    }

}
