package fr.glady.wedoogift.controllers;

import fr.glady.wedoogift.models.User;
import fr.glady.wedoogift.models.requests.DepositRequest;
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

    @PostMapping(path = "/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public User addDepositToUser(@RequestBody DepositRequest depositRequest) {
        log.info("add deposit to user with name : {}", depositRequest.getUsername());
        return userService.addDepositToUser(depositRequest);
    }

    @GetMapping(path = "/deposit/balance")
    @ResponseStatus(HttpStatus.OK)
    public double getGiftBalance(@RequestBody DepositRequest depositRequest) {
        log.info("Get deposit balance from user with name : {} and type : {}", depositRequest.getUsername(),
                depositRequest.getType().toString());
        return userService.getDepositBalanceFromUser(depositRequest.getUsername(), depositRequest.getType());
    }

}
