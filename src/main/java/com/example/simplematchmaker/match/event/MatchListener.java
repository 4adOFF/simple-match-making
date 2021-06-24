package com.example.simplematchmaker.match.event;

import com.example.simplematchmaker.match.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MatchListener {
    @Autowired
    private MatchService matchService;

    @EventListener
    public void handleContextStart(TryMatchEvent tme) {
        matchService.tryMatch();
    }
}
