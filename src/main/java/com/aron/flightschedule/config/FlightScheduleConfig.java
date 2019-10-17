package com.aron.flightschedule.config;

import com.aron.flightschedule.service.FlightScheduleStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(FlightScheduleStreams.class)
public class FlightScheduleConfig {

}
