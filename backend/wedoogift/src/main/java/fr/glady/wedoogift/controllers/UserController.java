package fr.glady.wedoogift.controllers;

import fr.glady.wedoogift.models.User;
import fr.glady.wedoogift.models.requests.GiftRequest;
import fr.glady.wedoogift.models.requests.UserRequest;
import fr.glady.wedoogift.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/users", produces = "application/json; charset=UTF-8")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody UserRequest userRequest) {
        log.info("Create user with name : {}", userRequest.getName());
        return userService.createUser(userRequest.getName());
    }

    @PostMapping(path = "/gift")
    @ResponseStatus(HttpStatus.CREATED)
    public User addGiftToUser(@RequestBody GiftRequest giftRequest) {
        log.info("add gift to user with name : {}", giftRequest.getUsername());
        return userService.addGiftToUser(giftRequest);
    }

    @GetMapping(path = "/gift/balance")
    @ResponseStatus(HttpStatus.OK)
    public double getGiftBalance(@RequestBody UserRequest userRequest) {
        log.info("Get gift balance from user with name : {}", userRequest.getName());
        return userService.getGiftBalanceFromUser(userRequest.getName());
    }

}
