package com.athul.memegramspring.repository;

import com.athul.memegramspring.entity.Category;
import com.athul.memegramspring.entity.Post;
import com.athul.memegramspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post,Integer> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);

}
