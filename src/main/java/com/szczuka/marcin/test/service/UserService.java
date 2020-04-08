package com.szczuka.marcin.test.service;

import com.szczuka.marcin.test.dto.CreateUserDto;
import com.szczuka.marcin.test.dto.UserDto;
import com.szczuka.marcin.test.exception.SubscriptionAlreadyExistsException;
import com.szczuka.marcin.test.exception.UserNotExistsException;
import com.szczuka.marcin.test.exception.UserNotFoundException;

public interface UserService {

    UserDto createUser(CreateUserDto newUser);
    UserDto getUserById(Long id) throws UserNotFoundException;
    void followUser(Long userId, Long followedUserId) throws UserNotExistsException, SubscriptionAlreadyExistsException;
}
