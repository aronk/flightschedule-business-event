package com.aron.flightschedule.model;

import org.springframework.stereotype.Component;

import static com.aron.flightschedule.model.FlightSchedule.FlightScheduleStatus.SCHEDULED;
import static com.aron.flightschedule.model.FlightScheduleBusinessEvent.BusinessEventType.FLIGHT_DELAYED;

@Component
public class FlightScheduleTransformer {

    public FlightSchedule fromFlightScheduleDataEvent(FlightScheduleDataEvent flightScheduleDataEvent) {

        return FlightSchedule.builder()
                .id(flightScheduleDataEvent.getFlightScheduleId())
                .flightNumber(flightScheduleDataEvent.getFlightNumber())
                .departureDate(flightScheduleDataEvent.getDepartureDate())
                .scheduledDepartureTime(flightScheduleDataEvent.getScheduledDepartureTime())
                .estimatedDepartureTime(flightScheduleDataEvent.getEstimatedDepartureTime())
                .originPort(flightScheduleDataEvent.getOriginPort())
                .originCountry(flightScheduleDataEvent.getOriginCountry())
                .destinationPort(flightScheduleDataEvent.getDestinationPort())
                .destinationCountry(flightScheduleDataEvent.getDestinationCountry())
                .airline(flightScheduleDataEvent.getAirline())
                .status(SCHEDULED)
                .build();
    }

    public FlightScheduleBusinessEvent toFlightDelayedBusinessEvent(FlightSchedule flightSchedule) {

        return FlightScheduleBusinessEvent.builder()
                .eventType(FLIGHT_DELAYED.name())
                .flightDelayed(FlightDelayedBusinessEvent.builder()
                        .flightScheduleId(flightSchedule.getId())
                        .flightNumber(flightSchedule.getFlightNumber())
                        .departureDate(flightSchedule.getDepartureDate())
                        .scheduledDepartureTime(flightSchedule.getScheduledDepartureTime())
                        .estimatedDepartureTime(flightSchedule.getEstimatedDepartureTime())
                        .originPort(flightSchedule.getOriginPort())
                        .originCountry(flightSchedule.getOriginCountry())
                        .destinationPort(flightSchedule.getDestinationPort())
                        .destinationCountry(flightSchedule.getDestinationCountry())
                        .airline(flightSchedule.getAirline())
                        .flightScheduleStatus(getFlightScheduleStatusString(flightSchedule))
                        .build())
                .build();
    }

    private String getFlightScheduleStatusString(FlightSchedule flightSchedule) {
        if (flightSchedule != null
                && flightSchedule.getStatus() != null) {
            return flightSchedule.getStatus().name();
        }
        return null;
    }

}
