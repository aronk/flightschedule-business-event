package com.aron.flightschedule.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class FlightDelayedBusinessEvent {

    private Long flightScheduleId;
    private String flightNumber;
    private LocalDate departureDate;
    private LocalDateTime scheduledDepartureTime;
    private LocalDateTime estimatedDepartureTime;
    private String originPort;
    private String originCountry;
    private String destinationPort;
    private String destinationCountry;
    private String airline;
    private String flightScheduleStatus;

}
