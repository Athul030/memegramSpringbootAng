package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.Like;
import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepo extends JpaRepository<Like,Integer> {

    Optional<Like> findByUserAndPost(User user, Post post);
}
