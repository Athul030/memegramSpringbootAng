package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c JOIN c.post p WHERE p.postId = :postId")
    List<Comment> findByPost(int postId);
}
