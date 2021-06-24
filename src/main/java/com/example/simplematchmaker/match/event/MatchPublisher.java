package com.example.simplematchmaker.match.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MatchPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent() {
        TryMatchEvent tryMatchEvent = new TryMatchEvent(new Object());
        applicationEventPublisher.publishEvent(tryMatchEvent);
    }
}
