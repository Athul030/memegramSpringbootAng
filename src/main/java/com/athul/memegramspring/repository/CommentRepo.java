package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

}
