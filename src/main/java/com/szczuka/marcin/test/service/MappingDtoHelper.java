package com.szczuka.marcin.test.service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.szczuka.marcin.test.dto.PostDto;
import com.szczuka.marcin.test.dto.UserDto;
import com.szczuka.marcin.test.entity.Follower;
import com.szczuka.marcin.test.entity.Post;
import com.szczuka.marcin.test.entity.User;

public class MappingDtoHelper {

    public static PostDto postToPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .userName(post.getCreator().getName())
                .userId(post.getCreator().getId())
                .time(post.getTime())
                .build();
    }

    public static UserDto userToUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .id(user.getId())
                .posts(user.getPosts().stream().map(MappingDtoHelper::postToPostDto).collect(Collectors.toList()))
                .followingUsers(user.getFollowedUsers().stream().map(Follower::getFollowedUserId).collect(Collectors.toList()))
                .build();
    }

    public static UserDto getFollowedUsers(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
