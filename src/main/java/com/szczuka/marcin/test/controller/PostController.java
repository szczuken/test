package com.szczuka.marcin.test.controller;

import javax.validation.Valid;
import java.util.List;

import com.szczuka.marcin.test.dto.CreatePostDto;
import com.szczuka.marcin.test.dto.PostDto;
import com.szczuka.marcin.test.exception.UserNotExistsException;
import com.szczuka.marcin.test.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid CreatePostDto newPost) throws UserNotExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(newPost));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto>> getUserPost(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(postService.findPostByUserId(userId));
    }

    @GetMapping("/user/{userId}/timeline")
    public ResponseEntity<List<PostDto>> getFollowedUsersPosts(
            @PathVariable("userId") Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page
    ) throws UserNotExistsException {
        return ResponseEntity.ok(postService.findPostFollowedUsersByUserId(userId, page));
    }
}
