package com.szczuka.marcin.test.service;

import java.util.List;

import com.szczuka.marcin.test.dto.CreatePostDto;
import com.szczuka.marcin.test.dto.PostDto;
import com.szczuka.marcin.test.exception.UserNotExistsException;

public interface PostService {

    PostDto createPost(CreatePostDto newPost) throws UserNotExistsException;
    List<PostDto> findPostByUserId(Long userId);
    List<PostDto> findPostFollowedUsersByUserId(Long userId, int page) throws UserNotExistsException;
}
