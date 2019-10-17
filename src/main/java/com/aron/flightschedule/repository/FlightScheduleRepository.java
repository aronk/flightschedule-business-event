package com.aron.flightschedule.repository;

import com.aron.flightschedule.model.FlightSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FlightScheduleRepository extends MongoRepository<FlightSchedule, Long> {

}