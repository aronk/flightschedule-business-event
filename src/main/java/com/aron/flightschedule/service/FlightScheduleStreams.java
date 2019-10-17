package com.aron.flightschedule.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface FlightScheduleStreams {

    String TOPICIN_SUB = "topicin-sub";
    String TOPICIN_PUB = "topicin-pub";
    String TOPICOUT_SUB = "topicout-sub";
    String TOPICOUT_PUB = "topicout-pub";

    @Input(TOPICIN_SUB)
    SubscribableChannel topicInSubscribe();

    @Output(TOPICIN_PUB)
    MessageChannel topicInPublish();

    @Input(TOPICOUT_SUB)
    SubscribableChannel topicOutSubscribe();

    @Output(TOPICOUT_PUB)
    MessageChannel topicOutPublish();
}
