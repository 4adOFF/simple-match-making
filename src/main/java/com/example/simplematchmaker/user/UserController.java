package com.example.simplematchmaker.user;

import com.example.simplematchmaker.match.MatchService;
import com.example.simplematchmaker.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users")
    ResponseEntity newPlayerInPool(@RequestBody User newUser) {
        service.putUser(newUser);
        return ResponseEntity.ok().build();
    }
}
