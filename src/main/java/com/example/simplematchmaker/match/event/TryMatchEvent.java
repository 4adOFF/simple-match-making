package com.example.simplematchmaker.match.event;

import org.springframework.context.ApplicationEvent;

public class TryMatchEvent extends ApplicationEvent {

    public TryMatchEvent(Object source) {
        super(source);
    }
}
