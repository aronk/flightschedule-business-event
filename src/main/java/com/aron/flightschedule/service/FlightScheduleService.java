package com.aron.flightschedule.service;

import com.aron.flightschedule.model.FlightSchedule;
import com.aron.flightschedule.model.FlightScheduleBusinessEvent;
import com.aron.flightschedule.model.FlightScheduleDataEvent;
import com.aron.flightschedule.model.FlightScheduleTransformer;
import com.aron.flightschedule.repository.FlightScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.aron.flightschedule.util.FlightScheduleUtils.isDateTimeOutsideDeltaInMinutes;

@Service
@Slf4j
public class FlightScheduleService {

    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightScheduleTransformer flightScheduleTransformer;
    private final BusinessEventProducer businessEventProducer;
    private final Long estimatedDepartureTimeDeltaMins;

    public FlightScheduleService(FlightScheduleRepository flightScheduleRepository,
                                 FlightScheduleTransformer flightScheduleTransformer,
                                 BusinessEventProducer businessEventProducer,
                                 @Value("${fs.app.estimatedDepartureTimeDeltaMins:15}") Long estimatedDepartureTimeDeltaMins) {
        this.flightScheduleRepository = flightScheduleRepository;
        this.flightScheduleTransformer = flightScheduleTransformer;
        this.businessEventProducer = businessEventProducer;
        this.estimatedDepartureTimeDeltaMins = estimatedDepartureTimeDeltaMins;
        log.info("flightScheduleRepository={}, flightScheduleTransformer={}, businessEventProducer={}, estimatedDepartureTimeDeltaMins={}",
                flightScheduleRepository, flightScheduleTransformer, businessEventProducer, estimatedDepartureTimeDeltaMins);
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
            businessEventProducer.sendBusinessEvent(flightScheduleBusinessEvent);
        }

    }

    public Optional<FlightSchedule> getFlightSchedule(Long id) {
        return flightScheduleRepository.findById(id);
    }

}
