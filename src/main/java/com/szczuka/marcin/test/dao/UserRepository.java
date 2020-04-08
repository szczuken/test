package com.szczuka.marcin.test.dao;

import java.awt.print.Pageable;
import java.util.Optional;

import com.szczuka.marcin.test.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.posts p LEFT JOIN FETCH u.followedUsers fu WHERE u.id = :userId")
    Optional<User> getById(Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.followedUsers fu WHERE u.id = :userId")
    Optional<User> getFollowedUsersById(Long userId);
}

