package com.szczuka.marcin.test.service.impl;

import static com.szczuka.marcin.test.service.MappingDtoHelper.postToPostDto;

import java.util.List;
import java.util.stream.Collectors;

import com.szczuka.marcin.test.dao.PostRepository;
import com.szczuka.marcin.test.dao.UserRepository;
import com.szczuka.marcin.test.dto.CreatePostDto;
import com.szczuka.marcin.test.dto.PostDto;
import com.szczuka.marcin.test.entity.Follower;
import com.szczuka.marcin.test.entity.Post;
import com.szczuka.marcin.test.entity.User;
import com.szczuka.marcin.test.exception.UserNotExistsException;
import com.szczuka.marcin.test.service.MappingDtoHelper;
import com.szczuka.marcin.test.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private static final int PAGE_SIZE = 50;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PostDto createPost(CreatePostDto newPost) throws UserNotExistsException {

        User user = userRepository.getById(newPost.getUserId())
                .orElseThrow(() -> new UserNotExistsException(newPost.getUserId()));
        Post post = postRepository.save(Post.builder().creator(user).content(newPost.getContent()).build());
        return postToPostDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> findPostByUserId(Long userId, int page) {
        return postRepository.findByCreator_IdOrderByTimeDesc(userId, PageRequest.of(page, PAGE_SIZE)).stream()
                .map(MappingDtoHelper::postToPostDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> findPostFollowedUsersByUserId(Long userId, int page) throws UserNotExistsException {
        List<Long> followedUsers = userRepository.getFollowedUsersById(userId)
                .orElseThrow(() -> new UserNotExistsException(userId))
                .getFollowedUsers().stream()
                .map(Follower::getFollowedUserId)
                .collect(Collectors.toList());

        log.info("Users id for post search: {}", followedUsers);
        return postRepository.findByCreator_IdInOrderByTimeDesc(followedUsers, PageRequest.of(page, PAGE_SIZE)).stream()
                .map(MappingDtoHelper::postToPostDto)
                .collect(Collectors.toList());
    }
}
