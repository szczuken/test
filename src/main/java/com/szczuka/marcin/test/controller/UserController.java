package com.szczuka.marcin.test.controller;

import javax.validation.Valid;

import com.szczuka.marcin.test.dto.CreateUserDto;
import com.szczuka.marcin.test.dto.UserDto;
import com.szczuka.marcin.test.exception.SubscriptionAlreadyExistsException;
import com.szczuka.marcin.test.exception.UserNotExistsException;
import com.szczuka.marcin.test.exception.UserNotFoundException;
import com.szczuka.marcin.test.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Long userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/{userId}/follow/{followedUserId}")
    public ResponseEntity<Void> followUser(
            @PathVariable("userId") Long userId,
            @PathVariable("followedUserId") Long followedUserId
    ) throws UserNotExistsException, SubscriptionAlreadyExistsException {
        userService.followUser(userId, followedUserId);
        return ResponseEntity.ok().build();
    }
}
