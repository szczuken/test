package com.szczuka.marcin.test.service.impl;

import static com.szczuka.marcin.test.service.MappingDtoHelper.userToUserDto;

import com.szczuka.marcin.test.dao.UserRepository;
import com.szczuka.marcin.test.dto.CreateUserDto;
import com.szczuka.marcin.test.dto.UserDto;
import com.szczuka.marcin.test.entity.Follower;
import com.szczuka.marcin.test.entity.User;
import com.szczuka.marcin.test.exception.SubscriptionAlreadyExistsException;
import com.szczuka.marcin.test.exception.UserNotExistsException;
import com.szczuka.marcin.test.exception.UserNotFoundException;
import com.szczuka.marcin.test.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(CreateUserDto newUser) {

        User userToSave = User.builder()
                .name(newUser.getName())
                .build();
        log.info("new user: {}", newUser);
        log.info("user to save: {}", userToSave);

        User user = userRepository.save(userToSave);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) throws UserNotFoundException {
        User user = userRepository.getById(id).orElseThrow(UserNotFoundException::new);
        return userToUserDto(user);
    }

    @Override
    @Transactional
    public void followUser(Long userId, Long followedUserId)
            throws UserNotExistsException, SubscriptionAlreadyExistsException {
        User user = userRepository.getById(userId).orElseThrow(() -> new UserNotExistsException(userId));
        User followedUser = userRepository.getById(followedUserId).orElseThrow(() -> new UserNotExistsException(followedUserId));

        boolean subscriptionExist = user.getFollowedUsers().stream()
                .map(Follower::getFollowedUserId)
                .anyMatch(id ->id.equals(followedUser.getId()));

        if (subscriptionExist) {
            throw new SubscriptionAlreadyExistsException();
        } else {
            user.getFollowedUsers().add(Follower.builder()
                    .followerId(user.getId())
                    .followedUserId(followedUser.getId())
                    .build());
        }
    }
}
