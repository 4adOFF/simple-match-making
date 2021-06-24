package com.example.simplematchmaker.user;

import com.example.simplematchmaker.match.event.MatchPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private MatchPublisher publisher;

    public void putUser(User newUser) {
        newUser.setRequestTime(Instant.now());
        repo.save(newUser);
        publisher.publishEvent();
    }
}
