package com.szczuka.marcin.test.dao;

import java.util.List;

import com.szczuka.marcin.test.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> findByCreator_IdOrderByTimeDesc(Long userId);

    List<Post> findByCreator_IdInOrderByTimeDesc(List<Long> ids, Pageable pageable);
}
