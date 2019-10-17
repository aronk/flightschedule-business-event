package com.aron.flightschedule.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "fs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightSchedule {

    public enum FlightScheduleStatus {
        SCHEDULED, DELAYED
    }

    // TODO: could use @NotNull for validation of mandatory and formatting business rules

    @Id
    private Long id; // PK
    private String flightNumber;
    private LocalDate departureDate;
    private LocalDateTime scheduledDepartureTime;
    private LocalDateTime estimatedDepartureTime;
    private String originPort;
    private String originCountry;
    private String destinationPort;
    private String destinationCountry;
    private String airline;
    private FlightScheduleStatus status;

}
